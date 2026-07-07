package com.qidiangk.smart.aster.handler;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.qidiangk.smart.aster.constants.CallRouteProcessEnum;
import com.qidiangk.smart.aster.constants.VariableNameEnum;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.handler.service.AgiSupportImpl;
import com.qidiangk.smart.aster.pojo.UserIntent;
import com.qidiangk.smart.aster.service.ICallRouteService;
import com.qidiangk.smart.aster.service.IvrService;
import com.qidiangk.smart.aster.service.asterisk.IQueueService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.live.AsteriskQueue;
import org.asteriskjava.live.AsteriskServer;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.agi.annotation.Agi;
import top.jpower.core.asterisk.agi.fastagi.AgiAbstractScript;
import top.jpower.core.asterisk.agi.fastagi.support.AgiSupport;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.asterisk.utils.AgiContext;
import top.jpower.core.dbs.tenant.TenantBroker;
import top.jpower.core.util.utils.Fc;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import static com.qidiangk.smart.aster.constants.VariableNameEnum.PARAMS;
import static com.qidiangk.smart.aster.constants.VariableNameEnum.ROUTE_ID;
import static org.asteriskjava.live.QueueMemberState.DEVICE_NOT_INUSE;

@Slf4j
@Agi(value = "call.agi", support = AgiSupportImpl.class)
@Component
@RequiredArgsConstructor
public class AgiHandler extends AgiAbstractScript {

    private final ICallRouteService callRouteService;
    private final IvrService ivrService;
    private final AsteriskProperties asteriskProperties;
    private final IQueueService queueService;
    private final EndpointsDao endpointsDao;
    private final RedissonClient redissonClient;
    @Lazy
    @Autowired
    private AsteriskServer asteriskServer;

    @Override
    public void hangup(final AgiSupport agiSupport) {
        // 中断主线程
        log.info("电话挂断了,挂断线程=={}", agiSupport.getName());

        agiSupport.thread().interrupt();
        AgiContext.clear(agiSupport.channel());
    }

    @Override
    @SneakyThrows(Exception.class)
    public void service(final AgiSupport agiSupport) {
        String endpointName = StrUtil.subBetween(agiSupport.channel().getName(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);

        TenantBroker.runAs(endpoint.getTenantid(),tenantCode -> {
            // 1=呼入 2=呼出
            Integer type = Fc.toInt(agiSupport.getCallType(), CallRouteProcessEnum.INTEND.getValue());

            String recordingPath = agiSupport.request().getParameter("path");
            agiSupport.setVariable(VariableNameEnum.FILE_PATH.getName(), recordingPath);

            // 设置录音
            String file =  StrUtil.concat(true, asteriskProperties.getVoiceRootDir(), agiSupport.getPhone(), File.separator, "recording_", DateUtil.format(DateUtil.date(), "yyyyMMddHHmmss"),".wav");
            // 创建录音目录
            FileUtil.mkParentDirs(file);
            // 开始录音
            agiSupport.mixMonitor(file);
            agiSupport.setVariable(VariableNameEnum.FILE_PATH.getName(), file);


            // 来电手机号存到全局变量
            AgiContext.cache(agiSupport.channel()).put("recordingPath", file);
            AgiContext.cache(agiSupport.channel()).put("callerNum", agiSupport.getPhone());
            AgiContext.cache(agiSupport.channel()).put("timestart", DateUtil.date());
            AgiContext.cache(agiSupport.channel()).put("callType", type);
            AgiContext.cache(agiSupport.channel()).put("callId", agiSupport.getUniqueId());

            // TODO 关于在没有空闲坐席的情况下的处理逻辑合理的处理办法是应该放到TransferGranter里处理，配合页面配置来实现。目前为了快速上线暂时直接查所有坐席数量
            Collection<AsteriskQueue> queues = asteriskServer.getQueues();
            AtomicReference<Integer> kfNum = new AtomicReference<>(0);
            queues.forEach(asteriskQueue->{
                if (asteriskQueue != null && Fc.isNotEmpty(asteriskQueue.getMembers())) {
                    kfNum.updateAndGet(v -> Math.toIntExact(v + asteriskQueue.getMembers().stream().filter(asteriskQueueMember -> {
                        // 坐席没有使用并且不是暂停的就代表能接电话
                        boolean is = DEVICE_NOT_INUSE.equals(asteriskQueueMember.getState()) && !asteriskQueueMember.isPaused();
                        if (is) {
                            return !queueService.isPaused(asteriskQueue.getName(), asteriskQueueMember.getLocation());
                        }
                        return false;
                    }).count()));
                }
            });
            log.info("当前的坐席数量===>>{}", kfNum.get());
            AgiContext.cache(agiSupport.channel()).put("kfNum", kfNum.get());

            String paramsId = agiSupport.getVariable(PARAMS.getName());
            Map<String, Object> params = redissonClient.<Map<String, Object>>getBucket("call:variable:"+paramsId).getAndDelete();
            log.info("外呼初始参数===={}===>>{}", paramsId, params);
            AgiContext.cache(agiSupport.channel()).put(PARAMS.getName(), params);

            // 获取流程
            Long routeId = Fc.toLong(agiSupport.getVariable(ROUTE_ID.getName()));

            if (Fc.notNull(routeId)) {
                CallRouteProcessEnum callRouteProcessEnum = CallRouteProcessEnum.valueOf(type);
                log.info("接受到来电==={},呼叫类型=={},呼叫路由={}", agiSupport.getPhone(), callRouteProcessEnum.getName(), routeId);

                // 接通电话
                agiSupport.answer();

                if (callRouteService.existsFlowById(routeId, callRouteProcessEnum)) {
                    List<? extends UserIntent.Node> flowObj = callRouteService.findCompleteFlow(routeId);
                    ivrService.executeNode(agiSupport, flowObj);
                } else {
                    log.warn("[{}]未查找到流程，电话直接挂断.......", agiSupport.getPhone());
                    agiSupport.hangup();
                }
            } else {
                log.warn("[{}]{}未传递线路ID，电话直接挂断.......", agiSupport.getPhone(), agiSupport.channel().getName());
                agiSupport.hangup();
            }
        });

    }

}
