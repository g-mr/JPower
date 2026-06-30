package com.qidiangk.smart.aster.listener;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.qidiangk.smart.aster.constants.*;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallEndDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStartDTO;
import com.qidiangk.smart.aster.pojo.dto.NoticeCallStatusDTO;
import com.qidiangk.smart.aster.service.ExteriorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.*;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.ami.annotation.AmiEvent;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.util.utils.Fc;

import java.util.Optional;

/**
 * 对外通话记录通知
 */
@Slf4j
@Component
@AmiListener({NewChannelEvent.class,
        NewCallerIdEvent.class,
        NewStateEvent.class,
        QueueCallerJoinEvent.class,
        AgentCalledEvent.class,
        HoldEvent.class,
        UnholdEvent.class,
        VarSetEvent.class,
        HangupRequestEvent.class,
        HangupEvent.class})
@RequiredArgsConstructor
public class NoticeCallListener extends CallListener {

    private final ExteriorService exteriorService;
    private final EndpointsDao endpointsDao;

    @AmiEvent(NewChannelEvent.class)
    public void newChannelEvent(NewChannelEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        // 通知的话只需要只需要通知外线的端点
        if (EndpointsTypeEnum.LINE.getName().equals(endpoint.getBusinessType())) {
            NoticeCallStartDTO startDTO = new NoticeCallStartDTO()
                    .setCallId(event.getLinkedid())
                    .setStartTime(DateUtil.date())
                    .setTrunkName(endpoint.getCallerid());

            NoticeCallStatusDTO statusDTO = null;

            if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_RING)) {
                // 呼入
                startDTO.setDirection(CallTypeEnum.IN);
                startDTO.setFrom(event.getCallerIdNum());
                startDTO.setAiOutbound(Boolean.TRUE);

                // 设置响铃状态
                statusDTO = new NoticeCallStatusDTO();
                statusDTO.setCallId(event.getLinkedid());
                statusDTO.setStatus(NoticeStatusEnum.RING);
                statusDTO.setStartTime(DateUtil.date());
            } else {
                // 呼出
                startDTO.setDirection(CallTypeEnum.OUT);
                startDTO.setTo(event.getCallerIdNum());
                if (Fc.isNotEmpty(CACHE.get(event.getLinkedid()))) {
                    // 获取被叫
                    Optional<NewCallerIdEvent> callerIdEvent = CACHE.get(event.getLinkedid())
                            .stream().filter(e -> e instanceof NewCallerIdEvent).map(e -> (NewCallerIdEvent) e).findFirst();
                    callerIdEvent.ifPresent(e -> startDTO.setTo(e.getCallerIdNum()));

                    // 获取主叫
                    Optional<NewChannelEvent> channelEvent = CACHE.get(event.getLinkedid()).stream().filter(e -> e instanceof NewChannelEvent).map(e -> (NewChannelEvent) e).findFirst();
                    channelEvent.ifPresent(e -> startDTO.setFrom(StrUtil.subBetween(e.getChannel(), "/", "-")));
                }

                // 获取是否AI
                if (Fc.isEmpty(CACHE.get(event.getLinkedid())) || CACHE.get(event.getLinkedid()).stream().noneMatch(e -> e instanceof NewChannelEvent)) {
                    // 如果第一个事件是外线的DOWN，则说明是机器人呼出
                    startDTO.setAiOutbound(Boolean.TRUE);
                } else {
                    // 如果第一个事件不是外线DOWN，则说明是外线呼出
                    startDTO.setAiOutbound(Boolean.FALSE);
                }
            }

            // 调用开始接口,如果是AI呼出，则不需要调用开始接口，交给NewCallerIdEvent事件处理
            if (!(Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_CALL) &&
                    (Fc.isEmpty(CACHE.get(event.getLinkedid())) || CACHE.get(event.getLinkedid()).stream().noneMatch(e -> e instanceof NewChannelEvent)))) {
                exteriorService.callReport(startDTO);
            }
            if (statusDTO != null) {
                // 调用状态接口
                exteriorService.callStatus(statusDTO);
            }
        }
    }

    /**
     * 解决AI呼出
     *
     * @param event
     */
    @AmiEvent(NewCallerIdEvent.class)
    public void newCallerIdEvent(NewCallerIdEvent event){
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        // 如果当前是外线、历史事件第一个NewChannelEvent事件是外线并且是DOWN状态，说明是机器人呼出
        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())) {
            CACHE.get(event.getLinkedId()).stream().filter(e -> e instanceof NewChannelEvent).map(e -> (NewChannelEvent) e).findFirst().ifPresent(e -> {
                if (Fc.equalsValue(e.getChannel(), event.getChannel()) && Fc.equalsValue(e.getChannelState(), CHANNEL_STATE_CALL)) {
                    // 进来就说明是AI呼出
                    NoticeCallStartDTO startDTO = new NoticeCallStartDTO()
                            .setCallId(event.getLinkedId())
                            .setStartTime(DateUtil.date())
                            .setTrunkName(endpoint.getCallerid())
                            .setFrom(null)
                            .setTo(event.getCallerIdNum())
                            .setDirection(CallTypeEnum.OUT)
                            .setAiOutbound(Boolean.TRUE);
                    exteriorService.callReport(startDTO);
                }
            });
        }
    }

    /**
     * 获取状态
     *
     * @param event
     */
    @AmiEvent(NewStateEvent.class)
    public void newStateEvent(NewStateEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())) {
            NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
            statusDTO.setCallId(event.getLinkedId());
            statusDTO.setStartTime(DateUtil.date());
            if (CHANNEL_STATE_RINGING.equals(event.getChannelState())) {
                statusDTO.setStatus(NoticeStatusEnum.RING);
            } else {
                statusDTO.setStatus(NoticeStatusEnum.ANSWER);
            }

            // 调用状态接口
            exteriorService.callStatus(statusDTO);
        } else {
            if (CACHE.get(event.getLinkedId()).stream().anyMatch(e -> e instanceof AgentCalledEvent)) {
                NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
                statusDTO.setCallId(event.getLinkedId());
                statusDTO.setStartTime(DateUtil.date());
                statusDTO.setStatus(NoticeStatusEnum.TRANSFER);
                statusDTO.setTransfer(new NoticeCallStatusDTO.Transfer()
                        .setExtNum(endpointName)
                        .setStatus(CHANNEL_STATE_RINGING.equals(event.getChannelState())?NoticeStatusEnum.RING:NoticeStatusEnum.ANSWER));

                // 调用状态接口
                exteriorService.callStatus(statusDTO);
            }
        }
    }

    /**
     * 转人工进入队列
     *
     * @param event
     */
    @AmiEvent(QueueCallerJoinEvent.class)
    public void queueCallerJoinEvent(QueueCallerJoinEvent event) {
        NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
        statusDTO.setCallId(event.getLinkedId());
        statusDTO.setStartTime(DateUtil.date());
        statusDTO.setStatus(NoticeStatusEnum.TRANSFER);
        statusDTO.setTransfer(new NoticeCallStatusDTO.Transfer()
                .setStatus(NoticeStatusEnum.READY));

        // 调用状态接口
        exteriorService.callStatus(statusDTO);
    }

    /**
     * 通话保持
     *
     * @param event
     */
    @AmiEvent(HoldEvent.class)
    public void holdEvent(HoldEvent event) {
        NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
        statusDTO.setCallId(event.getLinkedId());
        statusDTO.setStartTime(DateUtil.date());

        // 是否转了人工
        if (CACHE.get(event.getLinkedId()).stream().anyMatch(e -> e instanceof QueueCallerJoinEvent)){
            statusDTO.setStatus(NoticeStatusEnum.TRANSFER);
            statusDTO.setTransfer(new NoticeCallStatusDTO.Transfer()
                    .setExtNum(StrUtil.subBetween(event.getChannel(), "/", "-"))
                    .setStatus(NoticeStatusEnum.HOLD));
        } else {
            statusDTO.setStatus(NoticeStatusEnum.HOLD);
        }

        // 调用状态接口
        exteriorService.callStatus(statusDTO);
    }

    /**
     * 通话保持恢复
     *
     * @param event
     */
    @AmiEvent(UnholdEvent.class)
    public void unholdEvent(UnholdEvent event) {
        NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
        statusDTO.setCallId(event.getLinkedId());
        statusDTO.setStartTime(DateUtil.date());

        // 是否转了人工
        if (CACHE.get(event.getLinkedId()).stream().anyMatch(e -> e instanceof QueueCallerJoinEvent)){
            statusDTO.setStatus(NoticeStatusEnum.TRANSFER);
            statusDTO.setTransfer(new NoticeCallStatusDTO.Transfer()
                    .setExtNum(StrUtil.subBetween(event.getChannel(), "/", "-"))
                    .setStatus(NoticeStatusEnum.UN_HOLD));
        } else {
            statusDTO.setStatus(NoticeStatusEnum.UN_HOLD);
        }

        // 调用状态接口
        exteriorService.callStatus(statusDTO);
    }

    /**
     * 挂断事件
     *
     * @param event
     */
    @AmiEvent(HangupEvent.class)
    public void hangupEvent(HangupEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        // 是否是外线
        if (endpoint.getBusinessType().equals(EndpointsTypeEnum.LINE.getName())) {
            NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
            statusDTO.setCallId(event.getLinkedId());
            statusDTO.setStartTime(DateUtil.date());
            statusDTO.setStatus(NoticeStatusEnum.HANG);
            Optional<HangupRequestEvent> hangupRequestEvent = CACHE.get(event.getLinkedId()).stream().filter(e -> e instanceof HangupRequestEvent).findFirst().map(e -> (HangupRequestEvent) e);
            if (hangupRequestEvent.isPresent()) {
                EndpointsDO endpointsDO = endpointsDao.getById(StrUtil.subBetween(hangupRequestEvent.get().getChannel(), "/", "-"));
                if (endpointsDO != null && endpointsDO.getBusinessType().equals(EndpointsTypeEnum.ATTEND.getName())){
                    statusDTO.setExtHungFlag(Boolean.TRUE);
                } else {
                    statusDTO.setExtHungFlag(Boolean.FALSE);
                }
            } else {
                statusDTO.setExtHungFlag(Boolean.TRUE);
            }
            if (HANGUP_CAUSE_0.equals(event.getCause())) {
                // 如果是0的话需要看通道的最后的状态是不是接起来，如果接起来了说明是机器人主动的挂断了，如果没有接起来只是响铃了说明是听到了回铃声但是没接起来
                if (CHANNEL_STATE_ANSWER.equals(event.getChannelState())) {
                    statusDTO.setByeCause(CallHangStateEnum.NORMALLY);
                    // 说明是机器人主动挂断
                    statusDTO.setExtHungFlag(Boolean.TRUE);
                } else if (CHANNEL_STATE_RINGING.equals(event.getChannelState())) {
                    statusDTO.setExtHungFlag(Boolean.FALSE);
                    statusDTO.setByeCause(CallHangStateEnum.UN_ANSWER);
                } else {
                    statusDTO.setByeCause(CallHangStateEnum.UNUSUAL);
                    statusDTO.setExtHungFlag(Boolean.FALSE);
                }
            } else {
                statusDTO.setByeCause(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
            }

            // 调用状态接口
            exteriorService.callStatus(statusDTO);


            // 挂断上报
            NoticeCallEndDTO noticeCallEndDTO = new NoticeCallEndDTO();
            NewChannelEvent channelEvent = CACHE.get(event.getLinkedId()).stream().filter(e -> e instanceof NewChannelEvent)
                    .map(e -> (NewChannelEvent) e).findFirst().orElseThrow();
            EndpointsDO channelEndpoint = endpointsDao.getById(StrUtil.subBetween(channelEvent.getChannel(), "/", "-"));
            if (channelEndpoint.getBusinessType().equals(EndpointsTypeEnum.LINE.getName())) {
                if (CHANNEL_STATE_RING.equals(channelEvent.getChannelState())) {
                    // 呼入
                    noticeCallEndDTO.setFrom(channelEvent.getCallerIdNum());

                    // 从进入队列开始找到最后一个通道。就是最后的人工分机
                    int lstJoinIndex = lastIndexOf(event.getLinkedId(), e -> e instanceof QueueCallerJoinEvent);
                    if (lstJoinIndex >= 0) {
                        CACHE.get(event.getLinkedId()).subList(lstJoinIndex + 1, CACHE.get(event.getLinkedId()).size())
                                .stream()
                                .filter(e -> e instanceof NewChannelEvent)
                                .reduce((first, second) -> second)
                                .map(e -> (NewChannelEvent) e)
                                .ifPresent(e->{
                                    // 最后一个转人工的分机
                                    noticeCallEndDTO.setTo(StrUtil.subBetween(e.getChannel(), "/", "-"));
                                });
                    }
                } else {
                    // 机器人呼出
                    noticeCallEndDTO.setFrom(null); // 机器人没有分机号
                    noticeCallEndDTO.setTo(event.getCallerIdNum());
                }
            } else {
                noticeCallEndDTO.setFrom(channelEndpoint.getId());
                if (CHANNEL_STATE_RING.equals(channelEvent.getChannelState())) {
                    // 分机直拨
                    noticeCallEndDTO.setTo(event.getCallerIdNum());
                } else {
                    // 网页外拨
                    noticeCallEndDTO.setTo(event.getConnectedLineNum());
                }
            }
            Optional<String> filePath = CACHE.get(event.getLinkedId()).stream()
                    .filter(e -> e instanceof VarSetEvent)
                    .map(e -> (VarSetEvent) e)
                    .filter(e -> VariableNameEnum.FILE_PATH.getName().equals(e.getVariable()))
                    .map(VarSetEvent::getValue).findFirst();
            noticeCallEndDTO.setCallId(event.getLinkedId())
                    .setStartTime(channelEvent.getDateReceived())
                    .setEndTime(DateUtil.date())
                    .setDuration(DateUtil.between(channelEvent.getDateReceived(), event.getDateReceived(), DateUnit.SECOND))
                    .setUrl(filePath.orElse(null));
            // 调用上报接口
            exteriorService.hangupReport(noticeCallEndDTO);
            // 调文件上传接口
            exteriorService.uploadFile(event.getLinkedId(), filePath.orElse(null));
        } else {
            if (CACHE.get(event.getLinkedId()).stream().anyMatch(e -> e instanceof QueueCallerJoinEvent)) {
                // 如果是转过人工，并且是坐席的挂断，则说明是转接人工客服挂断了
                if (CACHE.get(event.getLinkedId()).stream().noneMatch(e-> {
                    if (e instanceof HangupRequestEvent hangupRequestEvent) {
                        EndpointsDO endpointsDO = endpointsDao.getById(StrUtil.subBetween(hangupRequestEvent.getChannel(), "/", "-"));
                        return endpointsDO != null && Fc.equalsValue(endpointsDO.getBusinessType(), EndpointsTypeEnum.LINE.getName());
                    }
                    return false;
                })) {
                    // 如果之前没有用户主动挂过，则通知转接人工挂断
                    NoticeCallStatusDTO statusDTO = new NoticeCallStatusDTO();
                    statusDTO.setCallId(event.getLinkedId());
                    statusDTO.setStartTime(DateUtil.date());
                    statusDTO.setStatus(NoticeStatusEnum.TRANSFER);
                    statusDTO.setTransfer(new NoticeCallStatusDTO.Transfer()
                            .setExtNum(endpointName)
                            .setExtHungFlag(Boolean.TRUE)
                            .setByeCause(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()))
                            .setStatus(NoticeStatusEnum.HANG));

                    // 调用状态接口
                    exteriorService.callStatus(statusDTO);
                }

            }
        }
    }

}
