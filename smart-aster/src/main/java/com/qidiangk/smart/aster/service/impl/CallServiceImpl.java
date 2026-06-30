package com.qidiangk.smart.aster.service.impl;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.map.MapBuilder;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.live.AsteriskChannel;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.LiveException;
import org.asteriskjava.live.OriginateCallback;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.HangupEvent;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import top.jpower.core.asterisk.ami.annotation.AmiEvent;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.asterisk.ami.listener.EventAbstractListener;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.EndpointsTypeEnum;
import com.qidiangk.smart.aster.constants.VariableNameEnum;
import com.qidiangk.smart.aster.dbs.dao.PhonePlaceDao;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.entity.PhonePlaceDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.vo.ivr.CallVO;
import com.qidiangk.smart.aster.service.CallService;
import com.qidiangk.smart.system.api.dto.CityDTO;
import com.qidiangk.smart.system.api.feign.SystemClient;

import java.time.Duration;
import java.util.Map;

import static com.qidiangk.smart.aster.constants.VariableNameEnum.LINE_ID;
import static com.qidiangk.smart.aster.constants.VariableNameEnum.PARAMS;
import static com.qidiangk.smart.aster.listener.CallListener.*;

/**
 * @author mr.g
 */
@Slf4j
@Service
@RequiredArgsConstructor
@AmiListener(value = {HangupEvent.class})
public class CallServiceImpl extends EventAbstractListener implements CallService {

    /**
     * 半小时缓存
     */
    private static final TimedCache<String, CallHangStateEnum> OUT_CALL_ID_CACHE = CacheUtil.newTimedCache(1000 * 60 * 30);

    @Lazy
    @Autowired
    private AsteriskServer asteriskServer;
    private final AsteriskProperties asteriskProperties;
    private final SystemClient systemClient;
    private final PhonePlaceDao phonePlaceDao;
    private final EndpointsDao endpointsDao;
    private final RedissonClient redissonClient;

    @Override
    public boolean isPhoneZero(String phone, EndpointsDO endpointsDO){
        if (Fc.isNull(endpointsDO) || !endpointsDO.getAddZero()) {
            return false;
        }

        String name = "";
        if (PhoneUtil.isTel(endpointsDO.getCallerid())) {
            String code = StrUtil.subPre(endpointsDO.getCallerid(), 4);
            R<CityDTO> city = systemClient.getCityByCode(code);
            if (city.isStatus() && Fc.notNull(city.getData())) {
                name = city.getData().getName();
            }
        } else {
            PhonePlaceDO phonePlaceDo = phonePlaceDao.getOneByField(PhonePlaceDO::getPhone, StrUtil.subPre(endpointsDO.getCallerid(), 7)+"0000");
            if (Fc.notNull(phonePlaceDo)) {
                name = phonePlaceDo.getCity();
            }
        }

        PhonePlaceDO phonePlaceDo = phonePlaceDao.getOneByField(PhonePlaceDO::getPhone, StrUtil.subPre(phone, 7)+"0000");
        return Fc.notNull(phonePlaceDo) && Fc.notEqualsValue(phonePlaceDo.getCity(), name);
    }

    @Override
    public CallVO call(String attendId, String phone, String lineId) {
        return call(phone, attendId, MapBuilder.<String, Object>create().put(LINE_ID.getName(), lineId).build(), true, false);
    }

    @Override
    public CallVO call(String phone, String endpointId, Map<String, Object> params) {
        return call(phone, endpointId, params, false, true);
    }

    @Override
    public CallVO call(String phone, String endpointId, Map<String, Object> params, boolean isAsync, boolean waitHang) {
        EndpointsDO endpointsDO = endpointsDao.getById(endpointId);

        if (endpointsDO == null) {
            return CallVO.builder()
                    .callResult(CallHangStateEnum.NOT_EXIST)
                    .build();
        }

        OriginateAction originateAction = new OriginateAction();
        log.info("call: {}", endpointsDO.getId());
        if (EndpointsTypeEnum.LINE.getName().equals(endpointsDO.getBusinessType())) {
            // 加前缀和0
            String goal = StrUtil.concat(true, endpointsDO.getPrefix(), phone);
            if (isPhoneZero(phone, endpointsDO)) {
                goal = "0"+goal;
            }
            log.info("呼叫目标=={}", goal);
            originateAction.setChannel("PJSIP/"+goal+"@"+endpointsDO.getId());
            originateAction.setExten(asteriskProperties.getContext().getExten());
        } else {
            originateAction.setChannel("PJSIP/"+endpointsDO.getId());
            originateAction.setExten(phone);
        }
        originateAction.setContext(asteriskProperties.getContext().getOut());
        originateAction.setPriority(1);
        originateAction.setCallerId(phone);
        originateAction.setTimeout(10000L);
        originateAction.setChannelId(IdUtil.fastSimpleUUID());


        VariableNameEnum.toArray().forEach(key -> {
            if (params.containsKey(key)) {
                originateAction.setVariable(key, Fc.toStr(params.remove(key)));
            }
        });


        // Variable通道只能存1024字节长度，超出会被截断，所以只能使用Redis来存
        String varId = IdUtil.getSnowflakeNextIdStr();
        redissonClient.getBucket("call:variable:"+varId).set(params, Duration.ofSeconds(1000*60));
        originateAction.setVariable(PARAMS.getName(), varId);


        CallVO callVO = CallVO.builder()
                .linkedId(originateAction.getChannelId())
                .build();
        if (waitHang) {
            OUT_CALL_ID_CACHE.put(callVO.getLinkedId(), CallHangStateEnum.NULL);
        }
        asteriskServer.originateAsync(originateAction, getCallback(callVO));

        if (isAsync) {
            return callVO;
        }

        if (waitHang) {
            while (!CallHangStateEnum.UNUSUAL.equals(callVO.getCallResult())
                    && (OUT_CALL_ID_CACHE.containsKey(callVO.getLinkedId())
                    && OUT_CALL_ID_CACHE.get(callVO.getLinkedId()) == CallHangStateEnum.NULL)) {
                ThreadUtil.safeSleep(1000);
            }
            callVO.setCallResult(OUT_CALL_ID_CACHE.get(callVO.getLinkedId()));
            // 删除缓存
            OUT_CALL_ID_CACHE.remove(callVO.getLinkedId());
            return callVO;
        }

        // 一分钟超时
        int timeout = 60;
        while (callVO.getCallResult() == null && timeout > 0) {
            ThreadUtil.safeSleep(1000);
            timeout--;
        }
        return callVO;
    }

    @AmiEvent(HangupEvent.class)
    public void hangupEvent(HangupEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }
        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())) {
            if (OUT_CALL_ID_CACHE.containsKey(event.getLinkedId())) {
                CallHangStateEnum stateEnum = CallHangStateEnum.getBySourceAsteriskCause(event.getCause());
                if (HANGUP_CAUSE_0.equals(event.getCause())) {
                    // 如果是0的话需要看通道的最后的状态是不是接起来，如果接起来了说明是机器人主动的挂断了，如果没有接起来只是响铃了说明是听到了回铃声但是没接起来
                    if (CHANNEL_STATE_ANSWER.equals(event.getChannelState())) {
                        stateEnum = CallHangStateEnum.NORMALLY;
                    } else if (CHANNEL_STATE_RINGING.equals(event.getChannelState())) {
                        stateEnum = CallHangStateEnum.UN_ANSWER;
                    } else {
                        stateEnum = CallHangStateEnum.UNUSUAL;
                    }
                }
                OUT_CALL_ID_CACHE.put(event.getLinkedId(), stateEnum);
            }
        }
    }

    private OriginateCallback getCallback(CallVO callVO) {
        return new OriginateCallback() {

            @Override
            public void onDialing(AsteriskChannel channel) {
            }

            @Override
            public void onSuccess(AsteriskChannel channel) {
                callVO.setCallResult(CallHangStateEnum.NORMALLY);
            }

            @Override
            public void onNoAnswer(AsteriskChannel channel) {
                callVO.setCallResult(CallHangStateEnum.NO_ANSWER);
            }

            @Override
            public void onBusy(AsteriskChannel channel) {
                callVO.setCallResult(CallHangStateEnum.BUSY);
            }

            @Override
            public void onFailure(LiveException cause) {
                callVO.setCallResult(CallHangStateEnum.UNUSUAL);
            }
        };
    }

}
