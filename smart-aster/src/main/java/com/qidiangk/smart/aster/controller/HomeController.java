package com.qidiangk.smart.aster.controller;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import com.mybatisflex.core.query.QueryMethods;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.boot.controller.BaseController;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.CallStateEnum;
import com.qidiangk.smart.aster.constants.CallTypeEnum;
import com.qidiangk.smart.aster.constants.EndpointsTypeEnum;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;
import com.qidiangk.smart.aster.pojo.vo.CountVO;
import com.qidiangk.smart.aster.pojo.vo.home.LineChatVO;
import com.qidiangk.smart.aster.pojo.vo.home.OverviewVO;
import com.qidiangk.smart.aster.pojo.vo.home.PieChatVO;
import com.qidiangk.smart.aster.service.ICallInfoService;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;
import com.qidiangk.smart.aster.service.asterisk.IQueueService;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ONLINE_AGENT;
import static com.qidiangk.smart.aster.dbs.entity.cdr.table.CallInfoDOTableDef.CALL_INFO_DO;
import static com.qidiangk.smart.aster.dbs.entity.cdr.table.CallTransferInfoDOTableDef.CALL_TRANSFER_INFO_DO;

/**
 * @author mr.g
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/home")
@Tag(name = "首页接口")
public class HomeController extends BaseController {

    private final RedissonClient redissonClient;
    private final ICallInfoService callInfoService;
    private final IPjSipService pjSipService;
    private final IQueueService queueService;

    @Operation(summary = "实时概览")
    @GetMapping("/overview")
    public R<OverviewVO> overview(){
        OverviewVO overviewVO = new OverviewVO();

        RMap<String, Object> map = redissonClient.getMap(ONLINE_AGENT);
        List<EndpointsDO> list = pjSipService.list(Wrappers.getQueryWrapper().eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName()));
        overviewVO.setTotalAgents(list.size());
        overviewVO.setBusyAgents(Fc.toInt(list.stream().filter(object -> map.containsKey(object.getId())).filter(object -> queueService.isAllPaused(object.getId())).count()));
        overviewVO.setIdleAgents(NumberUtil.sub(overviewVO.getOnlineAgents(), overviewVO.getBusyAgents()).intValue());
        overviewVO.setAiHandled(Fc.toInt(callInfoService.count(Wrappers.getQueryWrapper()
                .lt(CallInfoDO::getState, CallStateEnum.HANG.getValue())
                .isNull(CallInfoDO::getExtension)
                .eq(CallInfoDO::getIsTransfer, Boolean.FALSE)
                .and(QueryMethods.notExists(QueryMethods.selectOne().from(CallTransferInfoDO.class).and(CallTransferInfoDO::getCallInfoId).eq(CallInfoDO::getId)))
                ), 0));

        overviewVO.setOnlineAgents(Fc.toInt(list.stream().filter(object -> map.containsKey(object.getId())).count()));
        List<CallInfoDO> listCalls = callInfoService.list(Wrappers.getQueryWrapper().ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date())));
        overviewVO.setTotalCalls(listCalls.size());
        overviewVO.setAvgDuration(listCalls.stream().filter(CallInfoDO -> Fc.notNull(CallInfoDO.getAnswerTime()) && Fc.notNull(CallInfoDO.getHangupTime())).mapToLong(CallInfoDO -> {
            return DateUtil.between(CallInfoDO.getAnswerTime(), CallInfoDO.getHangupTime(), DateUnit.SECOND, true);
        }).average().orElse(0.0));
        overviewVO.setAiCalls(Fc.toInt(listCalls.stream().filter(CallInfoDO -> !CallInfoDO.getIsTransfer()).count(), 0));
        overviewVO.setSatisfaction(NumberUtil.mul(overviewVO.getAiCalls()>0?NumberUtil.div(overviewVO.getAiCalls(), overviewVO.getTotalCalls()).doubleValue():0.0, 100));
        long answerSize = listCalls.stream().filter(CallInfoDO -> Fc.notNull(CallInfoDO.getAnswerTime()) && Fc.equalsValue(CallInfoDO.getHangupState(), CallHangStateEnum.NORMALLY.getValue())).count();
        overviewVO.setConnectRate(NumberUtil.mul(answerSize>0?NumberUtil.div(answerSize, listCalls.size()):0.0, 100));

        // 昨日相比
        overviewVO.setAgentChange(overviewVO.getOnlineAgents() - Fc.toInt(redissonClient.getSet("onlineNum:" + DateUtil.yesterday().toDateStr()).size(), 0));
        List<CallInfoDO> listYesterdayCalls = callInfoService.list(Wrappers.getQueryWrapper().le(CallInfoDO::getCreateTime, DateUtil.endOfDay(DateUtil.yesterday())).ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.yesterday())));
        overviewVO.setCallChange(overviewVO.getTotalCalls() - listYesterdayCalls.size());
        overviewVO.setAiChange(overviewVO.getAiCalls() - Fc.toInt(listYesterdayCalls.stream().filter(CallInfoDO -> !CallInfoDO.getIsTransfer()).count(), 0));
        long answerYesterdaySize = listYesterdayCalls.stream().filter(CallInfoDO -> Fc.notNull(CallInfoDO.getAnswerTime()) && Fc.equalsValue(CallInfoDO.getHangupState(), CallHangStateEnum.NORMALLY.getValue())).count();
        overviewVO.setRateChange(overviewVO.getConnectRate() - NumberUtil.mul(answerYesterdaySize>0?NumberUtil.div(answerYesterdaySize, listYesterdayCalls.size()):0.0, 100));


        return R.data(overviewVO);
    }


    @Operation(summary = "通话类型分布")
    @GetMapping("/callTypePie")
    public R<Map<String, List<PieChatVO>>> callTypePie(){

        List<PieChatVO> chatVOs = callInfoService.listAs(Wrappers.getQueryWrapper()
                        .select(CallInfoDO::getType, CallInfoDO::getOutType)
                        .select(CALL_INFO_DO.ID.as(PieChatVO::getValue))
                        .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date()))
                        .groupBy(CallInfoDO::getType, CallInfoDO::getOutType),
                PieChatVO.class);



        List<PieChatVO> allList = chatVOs.stream().peek(chatVO -> {
            if (chatVO.getOutType() == null){
                chatVO.setName(chatVO.getType().getName());
            } else {
                chatVO.setName(chatVO.getOutType().getName());
            }
        }).toList();


        Map<CallTypeEnum, Integer> callTypeMap = chatVOs.stream().collect(Collectors.groupingBy(
                PieChatVO::getType,
                Collectors.summingInt(PieChatVO::getValue)
        ));

        List<PieChatVO> callTypeList = callTypeMap.entrySet().stream().map(entry -> {
            PieChatVO vo = new PieChatVO();
            vo.setName(entry.getKey().getName());
            vo.setValue(entry.getValue());
            return vo;
        }).toList();

        return R.data(Map.of("outType", allList, "type", callTypeList));
    }

    @Operation(summary = "通话趋势分析")
    @GetMapping("/callTrend")
    public R<Map<String, List<LineChatVO>>> callTrend(){

        List<LineChatVO> dateChat = callInfoService.listAs(Wrappers.getQueryWrapper()
                        .select(QueryMethods.hour(CallInfoDO::getCreateTime).as(LineChatVO::getName),
                                QueryMethods.sum(QueryMethods.if_(CALL_INFO_DO.IS_TRANSFER.eq(1), "1", "0")).as(LineChatVO::getAttendValue),
                                QueryMethods.sum(QueryMethods.if_(CALL_INFO_DO.IS_TRANSFER.eq(0).or(CALL_INFO_DO.IS_TRANSFER.isNull()), "1", "0")).as(LineChatVO::getAiValue))
                        .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date()))
                        .groupBy(QueryMethods.hour(CallInfoDO::getCreateTime)),
                LineChatVO.class);

        // 聚合成俩小时
        dateChat = dateChat.stream().collect(Collectors.groupingBy(
                        d -> Integer.parseInt(d.getName()) / 2,
                        TreeMap::new,
                        Collectors.teeing(
                                Collectors.summingInt(LineChatVO::getAiValue),      // 第一组聚合
                                Collectors.summingInt(LineChatVO::getAttendValue),   // 第二组聚合
                                (sum1, sum2) -> new LineChatVO("0", sum1, sum2)     // 合并结果
                        )
                ))
                .entrySet().stream()
                .map(e -> {
                    String hour = String.valueOf(e.getKey() * 2);
                    hour = hour.length() == 1 ? hour = "0" + hour + ":00" : hour + ":00";

                    return new LineChatVO(
                            hour,
                            e.getValue().getAiValue(),
                            e.getValue().getAttendValue()
                    );
                })
                .toList();


        Date lastWeek = DateUtil.offsetDay(DateUtil.lastWeek(), 1);
        List<LineChatVO> weekChat = callInfoService.listAs(Wrappers.getQueryWrapper()
                        .select(QueryMethods.dateFormat(CallInfoDO::getCreateTime, "%Y-%m-%d").as(LineChatVO::getName),
                                QueryMethods.sum(QueryMethods.if_(CALL_INFO_DO.IS_TRANSFER.eq(1), "1", "0")).as(LineChatVO::getAttendValue),
                                QueryMethods.sum(QueryMethods.if_(CALL_INFO_DO.IS_TRANSFER.eq(0).or(CALL_INFO_DO.IS_TRANSFER.isNull()), "1", "0")).as(LineChatVO::getAiValue))
                    .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(lastWeek))
                    .groupBy(QueryMethods.dateFormat(CallInfoDO::getCreateTime, "%Y-%m-%d")),
                LineChatVO.class);

        weekChat = weekChat.stream().peek(dp -> {
            dp.setName(DateUtil.parse(dp.getName()).dayOfWeekEnum().toChinese("周"));
        }).toList();

        return R.data(Map.of("dateChat", dateChat, "weekChat", weekChat));
    }


    @Operation(summary = "坐席效能排名")
    @GetMapping("/efficiency")
    public R<List<CountVO>> efficiency(){

        List<EndpointsDO> list = pjSipService.list(Wrappers.getQueryWrapper().eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName()));
        Map<String, String> map = list.stream().collect(Collectors.toMap(EndpointsDO::getId, EndpointsDO::getCallerid));
        List<CountVO> result = callInfoService.listAs(Wrappers.getQueryWrapper()
                        .select(CALL_INFO_DO.EXTENSION.as(CountVO::getName),
                                QueryMethods.count(CallInfoDO::getId).as(CountVO::getCount))
                        .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date()))
                        .eq(CallInfoDO::getIsTransfer, 1)
                        .isNotNull(CallInfoDO::getIsTransfer)
                        .groupBy(CallInfoDO::getExtension)
                        .having(QueryMethods.count(CallInfoDO::getId).gt(0))
                        .limit(10)
                        .orderBy(QueryMethods.column(CountVO::getCount).desc()),
                CountVO.class);

        return R.data(result);
    }


    @Operation(summary = "AI与人工占比分布")
    @GetMapping("/aiAttend")
    public R<List<CountVO>> aiAttend(){
        List<CountVO> list = callInfoService.listAs(Wrappers.getQueryWrapper()
                        .select(CALL_INFO_DO.IS_TRANSFER.as(CountVO::getName),
                                QueryMethods.count(CallInfoDO::getId).as(CountVO::getCount))
                .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date()))
                .eq(CallInfoDO::getHangupState, CallHangStateEnum.NORMALLY.getValue())
                .groupBy(CallInfoDO::getIsTransfer), CountVO.class);


        long count = callInfoService.count(Wrappers.getQueryWrapper().as("t")
                        .leftJoin(QueryMethods.select(CALL_TRANSFER_INFO_DO.CALL_INFO_ID,
                                QueryMethods.max(CALL_TRANSFER_INFO_DO.HANGUP_TIME).as(CallTransferInfoDO::getHangupTime))
                                .from(CallTransferInfoDO.class).isNotNull(CallTransferInfoDO::getHangupTime)
                                .groupBy(CallTransferInfoDO::getCallInfoId)).as("t1").on(CallTransferInfoDO::getCallInfoId, CallInfoDO::getId)
                .ge(CallInfoDO::getCreateTime, DateUtil.beginOfDay(DateUtil.date()))
                .eq(CallInfoDO::getHangupState, CallHangStateEnum.NORMALLY.getValue())
                .eq(CallInfoDO::getIsTransfer, 1)
                // 转人工的挂断时间比AI挂断晚一分钟算AI辅助
                .gt("TIMESTAMPDIFF(MINUTE, t1.hangupTime, t.hangup_time)", 1));
        list = list.stream().peek(item -> {
            item.setName(Fc.equalsValue(item.getName(), 1) ? "人工" : "AI");
        }).collect(Collectors.toList());
        list.add(new CountVO("AI辅助", (int) count));
        return R.data(list);
    }
}
