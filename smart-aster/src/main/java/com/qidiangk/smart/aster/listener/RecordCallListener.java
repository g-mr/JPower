package com.qidiangk.smart.aster.listener;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.qidiangk.smart.aster.constants.*;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.dao.cdr.CallInfoDao;
import com.qidiangk.smart.aster.dbs.dao.cdr.CallTransferInfoDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.*;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.ami.annotation.AmiEvent;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AmiListener({NewChannelEvent.class,
        NewCallerIdEvent.class,
        NewStateEvent.class,
        QueueCallerJoinEvent.class,
        QueueCallerAbandonEvent.class,
        QueueCallerLeaveEvent.class,
        AgentRingNoAnswerEvent.class,
        HoldEvent.class,
        UnholdEvent.class,
        VarSetEvent.class,
        HangupRequestEvent.class,
        HangupEvent.class})
@RequiredArgsConstructor
public class RecordCallListener extends CallListener {

    private final CallInfoDao callInfoDao;
    private final CallTransferInfoDao callTransferInfoDao;
    private final EndpointsDao endpointsDao;

    @AmiEvent(NewChannelEvent.class)
    public void newChannelEvent(NewChannelEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        EndpointsDO endpoint = endpointsDao.getById(endpointName);

        CallInfoDO callInfoDo = Optional.ofNullable(callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedid())).orElse(new CallInfoDO());
        callInfoDo.setLinkedId(event.getLinkedid());

        if (endpoint != null) {
            callInfoDo.setContext(event.getContext());
            if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_RING)) {
                // 收到了一个往外拨的电话新通道

                // 记录主叫的电话， 如果是外线这个字段就是呼入的真实手机号、如果是坐席这个字段记录的就是主叫坐席号
                callInfoDo.setPhone(event.getCallerIdNum());
                if (EndpointsTypeEnum.LINE.getName().equals(endpoint.getBusinessType())) {
                    // 如果是线路就证明是呼入
                    callInfoDo.setType(CallTypeEnum.IN);
                    // 如果是呼入就记录中继线路
                    callInfoDo.setChannelLine(endpointName);
                    callInfoDo.setChannelName(event.getChannel());
                    // 如果是线路呼入就证明响铃了
                    callInfoDo.setState(CallStateEnum.RING);
                    callInfoDo.setCallTime(DateUtil.date());
                    callInfoDo.setRingTime(DateUtil.date());
                } else {
                    // 如果是坐席就证明是呼出
                    // 呼出分俩种情况、往外呼、内部呼出；这里暂且都认为都是外呼,通过后续事件修正
                    callInfoDo.setType(CallTypeEnum.OUT);
                    // 如果是坐席呼出就证明通话开始
                    callInfoDo.setState(CallStateEnum.START);
                    callInfoDo.setCallTime(DateUtil.date());
                    // 记录被叫号码
                    callInfoDo.setPhone(event.getExten());
                    // 记录当前的分机号
                    callInfoDo.setExtension(event.getCallerIdNum());
                }

            }
            else if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_CALL)) {
                // 收到了一个来电的电话新通道

                if (EndpointsTypeEnum.LINE.getName().equals(endpoint.getBusinessType())) {
                    // 是否线路进来的第一个事件
                    if (Fc.isEmpty(CACHE.get(event.getLinkedid()))){
                        // 外线收到来电且是第一个事件的情况下说明是机器人在外呼

                        // 这里还拿不到具体外呼的手机号
                        callInfoDo.setType(CallTypeEnum.OUT);
                        callInfoDo.setOutType(CallOutTypeEnum.OUT_ROBOT);
                        callInfoDo.setState(CallStateEnum.START);
                        callInfoDo.setCallTime(DateUtil.date());
                    } else {
                        // 是来电事件而且是后续事件、而且是外线，说明是在外呼
                        callInfoDo.setType(CallTypeEnum.OUT);
                        CACHE.get(event.getLinkedid()).stream()
                                .filter(e -> e instanceof NewChannelEvent)
                                .filter(e -> Fc.equalsValue(CHANNEL_STATE_RING, e.getChannelState())).findFirst()
                                .ifPresent(e -> {
                                    // 如果之前的NewChannelEvent事件是呼出（Ring），就说明是在分机直拨呼出
                                    callInfoDo.setOutType(CallOutTypeEnum.OUT);
                        });
                    }
                    // 如果是线路就记录是哪个中继
                    callInfoDo.setChannelLine(endpointName);
                    callInfoDo.setChannelName(event.getChannel());
                }
                else {
                    // 坐席接受到了来电
                    if (Fc.isEmpty(CACHE.get(event.getLinkedid()))){
                        // 第一个事件是分机来电，证明这是分机通过网页在外呼，分机响铃了
                        callInfoDo.setType(CallTypeEnum.OUT);
                        callInfoDo.setExtension(endpointName);
                        callInfoDo.setOutType(CallOutTypeEnum.OUT_WEB);
                        callInfoDo.setState(CallStateEnum.START);
                        callInfoDo.setCallTime(DateUtil.date());
                    } else {
                        // 如果是坐席收到了来电，且不是第一个事件，要么是转人工、要么是内部通话
                        if (Fc.isBlank(callInfoDo.getChannelLine())) {
                            // 记录内部通话被叫
                            if (Fc.isNotEmpty(CACHE.get(event.getLinkedid()))) {
                                callInfoDo.setPhone(CACHE.get(event.getLinkedid()).getFirst().getCallerIdNum());
                                callInfoDo.setExtension(endpointName);
                            }
                            // 如果没有中继线路就说明是内部通话
                            callInfoDo.setType(CallTypeEnum.INNER);
                        } else {
                            // 有中继，且分机接到了来电而且是后续事件，说明是转人工了,也可能是呼入的电话直接转坐席了
                            // 记录最后一个坐席
                            callInfoDo.setExtension(endpointName);
                            callInfoDo.setIsTransfer(Boolean.TRUE);

                            // 先判断之前事件的最后一条队列事件是不是进入队列进去的，如果是就更新队列分配坐席；如果不是就说明是直接转的分机，需要新增一条记录
                            ManagerEvent managerEvent = CACHE.get(event.getLinkedid()).stream()
                                    .filter(e -> e instanceof QueueCallerJoinEvent || e instanceof QueueCallerLeaveEvent)
                                    .reduce((first, second) -> second).orElse(null);
                            CallTransferInfoDO callTransferInfoDo = new CallTransferInfoDO();
                            if (managerEvent instanceof QueueCallerJoinEvent joinEvent) {
                                callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                                        .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                                        .eq(CallTransferInfoDO::getState, CallStateEnum.START)
                                        .eq(CallTransferInfoDO::getQueue, joinEvent.getQueue())
                                        .isNull(CallTransferInfoDO::getExtension));
                            }
                            callTransferInfoDo.setCallInfoId(callInfoDo.getId());
                            callTransferInfoDo.setExtension(endpointName);
                            callTransferInfoDo.setUniqueId(event.getUniqueId());
                            callTransferInfoDo.setState(CallStateEnum.START);
                            callTransferInfoDo.setReadyTime(DateUtil.date());
                            callTransferInfoDao.saveOrUpdate(callTransferInfoDo);
                        }
                    }
                }

            } else {
                log.warn("正常不应该走到这里，遇到了没见过的状态，发现了需要看下是怎么打的这个电话==>>{}", JSONUtil.toJsonPrettyStr(event));
            }
        } else {
            log.warn("未知终端===>>{}", JSONUtil.toJsonPrettyStr(event));
        }

        // 保存记录
        callInfoDao.saveOrUpdate(callInfoDo);
    }

    /**
     * 取NewCallerIdEvent第一个事件的CallerIdNum为真实手机号
     * @param event
     */
    @AmiEvent(NewCallerIdEvent.class)
    public void newCallerIdEvent(NewCallerIdEvent event) {
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }

        if (!callInfoDo.getIsTransfer() && Fc.notEqualsValue(callInfoDo.getType(), CallTypeEnum.INNER)) {
            LinkedList<ManagerEvent> events = CACHE.get(event.getLinkedId());
            if (events.stream().noneMatch(e -> e instanceof NewCallerIdEvent)) {
                // 只取第一个NewCallerIdEvent事件
                callInfoDo.setPhone(event.getCallerIdNum());
                // 保存记录
                callInfoDao.updateById(callInfoDo);
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
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }

        // 只有真正得分机收到电话才算转了人工，所以这里注释掉
//        callInfoDo.setIsTransfer(Boolean.TRUE);
//        callInfoMapper.updateById(callInfoDo);

        CallTransferInfoDO transferInfoDo = new CallTransferInfoDO();
        transferInfoDo.setCallInfoId(callInfoDo.getId());
        transferInfoDo.setQueue(event.getQueue());
        transferInfoDo.setState(CallStateEnum.START);
        transferInfoDo.setReadyTime(DateUtil.date());
        callTransferInfoDao.save(transferInfoDo);
    }

    /**
     * 转人工没有成功，放弃队列呼叫
     *
     * 如果没有人接听（分机异常、拒接、超时）会有AgentRingNoAnswerEvent事件
     *
     * @param event
     */
    @AmiEvent(QueueCallerAbandonEvent.class)
    public void queueCallerAbandonEvent(QueueCallerAbandonEvent event) {
        List<ManagerEvent> list = CACHE.get(event.getLinkedId());
        int lastJoinIndex = lastIndexOf(event.getLinkedId(), e -> e instanceof QueueCallerJoinEvent);
        boolean result = lastJoinIndex < 0 || list.subList(lastJoinIndex + 1, list.size())
                .stream()
                .noneMatch(e -> e instanceof AgentRingNoAnswerEvent);

        if (result) {
            // 证明进入队列之后一直没有分配到坐席，坐席都在忙
            CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());

            CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                    .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                    .eq(CallTransferInfoDO::getQueue, event.getQueue())
                    .eq(CallTransferInfoDO::getState, CallStateEnum.START));
            if (callTransferInfoDo.getState().getValue() < CallStateEnum.HANG.getValue()) {
                callTransferInfoDo.setState(CallStateEnum.HANG);
            }
            callTransferInfoDo.setHangupTime(DateUtil.date());

            // 如果之前存在用户主得请求挂断就说明是用户主动挂断了，如果之前没有就说明是放弃队列呼叫了，有可能是队列满了也可能是拒接或者响铃超时
            Optional<HangupRequestEvent> hangupRequestEvent = CACHE.get(event.getLinkedId()).stream().filter(e -> e instanceof HangupRequestEvent)
                    .map(e -> (HangupRequestEvent) e).filter(e -> Fc.equalsValue(callInfoDo.getChannelName(), e.getChannel())).findFirst();
            if (hangupRequestEvent.isPresent()) {
                callTransferInfoDo.setHangupState(CallHangStateEnum.NORMALLY);
                callTransferInfoDo.setHangupExt(Boolean.FALSE);
            } else {

                // 如果最后一次进入队列之后出现过分机挂断事件，就证明不是坐席满了，交给挂断事件处理即可
                boolean is = list.subList(lastJoinIndex + 1, list.size())
                        .stream()
                        .noneMatch(e -> e instanceof HangupEvent);
                if (is) {
                    callTransferInfoDo.setHangupState(CallHangStateEnum.AGENT_FULL);
                    callTransferInfoDo.setHangupExt(Boolean.FALSE);
                }

            }

            callTransferInfoDao.updateById(callTransferInfoDo);
        }
    }

    /**
     * 获取IVR ID保存
     * @param event
     */
    @AmiEvent(VarSetEvent.class)
    public void varSetEvent(VarSetEvent event) {
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }

        if(VariableNameEnum.ROUTE_ID.getName().equals(event.getVariable())) {
            callInfoDo.setIvrId(Fc.toLong(event.getValue()));
            // 保存记录
            callInfoDao.updateById(callInfoDo);
        } else if (VariableNameEnum.FILE_PATH.getName().equals(event.getVariable())) {
            callInfoDo.setFilePath(event.getValue());
            // 保存记录
            callInfoDao.updateById(callInfoDo);
        } else if (VariableNameEnum.TRANSFER_FILE.getName().equals(event.getVariable())) {
            CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                    .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                    .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
            if (Fc.notNull(callTransferInfoDo)) {
                callTransferInfoDo.setFilePath(event.getValue());
                // 保存记录
                callTransferInfoDao.updateById(callTransferInfoDo);
            }
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
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }

        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())
            || CallTypeEnum.INNER.equals(callInfoDo.getType())) {
            // 如果是外线或者是内部通话，直接可以保存收到的状态
            if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_ANSWER)) {
                callInfoDo.setState(CallStateEnum.ANSWER);
                callInfoDo.setAnswerTime(DateUtil.date());
            } else if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_RINGING)) {
                callInfoDo.setState(CallStateEnum.RING);
                callInfoDo.setRingTime(DateUtil.date());
            } else {
                log.warn("收到一个不认识的状态==>>{}", JSONUtil.toJsonPrettyStr(event));
                // 直接返回不需要保存了
                return;
            }
        } else {
            // 如果是坐席，需要根据具体类型保存状态
            if (CallTypeEnum.OUT.equals(callInfoDo.getType()) && CallOutTypeEnum.OUT_WEB.equals(callInfoDo.getOutType())) {
                //如果是网页外呼则记录的是分机的状态
                if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_ANSWER)) {
                    callInfoDo.setState(CallStateEnum.EXTEN_ANSWER);
                } else if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_RINGING)) {
                    callInfoDo.setState(CallStateEnum.EXTEN_RING);
                } else {
                    log.warn("收到一个不认识的状态==>>{}", JSONUtil.toJsonPrettyStr(event));
                    // 直接返回不需要保存了
                    return;
                }
            } else {
                // 如果是坐席，并且不是网页外呼，并且有转人工记录的则说明这是呼入或者机器人呼出被转接到人工坐席了
                CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                                                            .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                                                            .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
                if (Fc.notNull(callTransferInfoDo)) {
                    //noinspection DuplicatedCode
                    if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_ANSWER)) {
                        callTransferInfoDo.setState(CallStateEnum.ANSWER);
                        callTransferInfoDo.setAnswerTime(DateUtil.date());
                    } else if (Fc.equalsValue(event.getChannelState(), CHANNEL_STATE_RINGING)) {
                        callTransferInfoDo.setState(CallStateEnum.RING);
                        callTransferInfoDo.setRingTime(DateUtil.date());
                    } else {
                        log.warn("收到一个不认识的状态==>>{}", JSONUtil.toJsonPrettyStr(event));
                        // 直接返回不需要保存了
                        return;
                    }
                    // 保存记录
                    callTransferInfoDao.updateById(callTransferInfoDo);
                    return;
                }
            }
        }

        // 保存记录
        callInfoDao.updateById(callInfoDo);

    }

    /**
     * 通话保持
     *
     * @param event
     */
    @AmiEvent(HoldEvent.class)
    public void holdEvent(HoldEvent event) {
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }
        callInfoDo.setState(CallStateEnum.HOLD);
        callInfoDao.updateById(callInfoDo);

        CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
        if (Fc.notNull(callTransferInfoDo)) {
            callTransferInfoDo.setState(CallStateEnum.HOLD);
            callTransferInfoDao.updateById(callTransferInfoDo);
        }
    }

    /**
     * 通话保持恢复
     *
     * @param event
     */
    @AmiEvent(UnholdEvent.class)
    public void unholdEvent(UnholdEvent event) {
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }

        callInfoDo.setState(CallStateEnum.ANSWER);
        // 保存记录
        callInfoDao.updateById(callInfoDo);

        CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
        if (Fc.notNull(callTransferInfoDo)) {
            callTransferInfoDo.setState(CallStateEnum.ANSWER);
            callTransferInfoDao.updateById(callTransferInfoDo);
        }
    }

    /**
     * 挂断请求事件
     * @param event
     */
    @AmiEvent(HangupRequestEvent.class)
    public void hangupRequestEvent(HangupRequestEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())) {
            callInfoDo.setState(CallStateEnum.HANG);
            callInfoDo.setHangupUser("user");
            callInfoDo.setHangupTime(DateUtil.date());
            callInfoDo.setHangupState(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
        } else {
            callInfoDo.setHangupUser(endpointName);

            CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                    .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                    .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
            if (Fc.notNull(callTransferInfoDo)) {
                callTransferInfoDo.setState(CallStateEnum.HANG);
                callTransferInfoDo.setHangupExt(Boolean.TRUE);
                callTransferInfoDo.setHangupTime(DateUtil.date());
                callTransferInfoDo.setHangupState(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
                callTransferInfoDao.updateById(callTransferInfoDo);
            } else {
                callInfoDo.setState(CallStateEnum.HANG);
                callInfoDo.setHangupUser(endpointName);
                callInfoDo.setHangupTime(DateUtil.date());
                callInfoDo.setHangupState(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
            }
        }
        // 保存记录
        callInfoDao.updateById(callInfoDo);
    }

    /**
     * 挂断事件
     *
     * @param event
     */
    @AmiEvent(HangupEvent.class)
    public void hangupEvent(HangupEvent event) {
        String endpointName = StrUtil.subBetween(event.getChannel(), "/", "-");
        CallInfoDO callInfoDo = callInfoDao.getOneByField(CallInfoDO::getLinkedId, event.getLinkedId());
        if (Fc.isNull(callInfoDo)) {
            log.warn("没有找到通话记录==>>{}", JSONUtil.toJsonPrettyStr(callInfoDo));
            return;
        }
        EndpointsDO endpoint = endpointsDao.getById(endpointName);
        if (Fc.isNull(endpoint)) {
            log.warn("没有找到终端==>>{}", JSONUtil.toJsonPrettyStr(event));
            return;
        }

        if (Fc.equalsValue(endpoint.getBusinessType(), EndpointsTypeEnum.LINE.getName())) {
            callInfoDo.setState(CallStateEnum.HANG);
            callInfoDo.setHangupTime(DateUtil.date());
            callInfoDo.setHangupCause(event.getCause() + " -> " + event.getCauseTxt());
            if (HANGUP_CAUSE_0.equals(event.getCause())) {
                // 如果是0的话需要看通道的最后的状态是不是接起来，如果接起来了说明是机器人主动的挂断了，如果没有接起来只是响铃了说明是听到了回铃声但是没接起来
                if (CHANNEL_STATE_ANSWER.equals(event.getChannelState())) {
                    callInfoDo.setHangupState(CallHangStateEnum.NORMALLY);
                    if (Fc.isBlank(callInfoDo.getHangupUser())) {
                        // 如果是空的就证明之前没有人挂断过是机器人挂断的
                        callInfoDo.setHangupUser("robot");
                    }
                } else if (CHANNEL_STATE_RINGING.equals(event.getChannelState())) {
                    callInfoDo.setHangupState(CallHangStateEnum.UN_ANSWER);
                } else {
                    callInfoDo.setHangupState(CallHangStateEnum.UNUSUAL);
                }
            } else {
                callInfoDo.setHangupState(CallHangStateEnum.getByAsteriskCause(event.getCause()));
                if (Fc.isBlank(callInfoDo.getHangupUser())) {
                    // 如果request事件没设置值，说明就没有这个事件就是机器人主动挂断的
                    callInfoDo.setHangupUser("robot");
                }
            }

        }
        else {
            CallTransferInfoDO callTransferInfoDo = callTransferInfoDao.getOne(Wrappers.getQueryWrapper()
                    .eq(CallTransferInfoDO::getCallInfoId, callInfoDo.getId())
                    .eq(CallTransferInfoDO::getUniqueId, event.getUniqueId()));
            if (Fc.notNull(callTransferInfoDo)) {
                // 这里不用处理主记录需要去处理对应的转接记录
                callTransferInfoDo.setState(CallStateEnum.HANG);
                boolean is = CACHE.get(event.getLinkedId()).stream().filter(e -> e instanceof HangupRequestEvent).anyMatch(e -> ((HangupRequestEvent) e).getChannel().equals(callInfoDo.getChannelName()));
                // 如果之前有用户得挂断请求就说明是用户主动挂断是正常，否则就走正常逻辑
                if (is) {
                    callTransferInfoDo.setHangupState(CallHangStateEnum.NORMALLY);
                    callTransferInfoDo.setHangupExt(Boolean.FALSE);
                } else {
                    callTransferInfoDo.setHangupState(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
                }
                callTransferInfoDo.setHangupCause(event.getCause() + " -> " + event.getCauseTxt());
                callTransferInfoDo.setHangupTime(DateUtil.date());
                callTransferInfoDao.updateById(callTransferInfoDo);
            }
            else {
                // 如果之前没有挂断事件话就执行,说明是第一个事件可以执行，执行的目的是防止内部只有一个事件的情况下也能抓获
                if (CACHE.get(event.getLinkedId()).stream().noneMatch(e -> e instanceof HangupEvent)) {
                    callInfoDo.setState(CallStateEnum.HANG);
                    callInfoDo.setHangupTime(DateUtil.date());
                    callInfoDo.setHangupCause(event.getCause() + " -> " + event.getCauseTxt());
                    callInfoDo.setHangupState(CallHangStateEnum.getBySourceAsteriskCause(event.getCause()));
                }
            }
        }

        // 保存记录
        callInfoDao.updateById(callInfoDo);
    }

}
