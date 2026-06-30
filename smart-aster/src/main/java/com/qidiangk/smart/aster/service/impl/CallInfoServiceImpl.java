package com.qidiangk.smart.aster.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.tenant.TenantManager;
import com.qidiangk.smart.aster.pojo.vo.cdr.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.CallStateEnum;
import com.qidiangk.smart.aster.dbs.dao.cdr.CallInfoDao;
import com.qidiangk.smart.aster.dbs.dao.cdr.CallTransferInfoDao;
import com.qidiangk.smart.aster.dbs.dao.cdr.mapper.CallInfoMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.EndpointsDO;
import com.qidiangk.smart.aster.dbs.entity.asterisk.QueuesDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;
import com.qidiangk.smart.aster.dbs.entity.ivr.CallRouteDO;
import com.qidiangk.smart.aster.pojo.vo.cdr.*;
import com.qidiangk.smart.aster.service.ICallInfoService;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;
import com.qidiangk.smart.aster.service.asterisk.IQueueService;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static com.qidiangk.smart.aster.dbs.entity.cdr.table.CallInfoDOTableDef.CALL_INFO_DO;
import static com.qidiangk.smart.aster.dbs.entity.cdr.table.CallTransferInfoDOTableDef.CALL_TRANSFER_INFO_DO;
import static com.qidiangk.smart.aster.dbs.entity.ivr.table.CallRouteDOTableDef.CALL_ROUTE_DO;

@Slf4j
@Service
@RequiredArgsConstructor
public class CallInfoServiceImpl extends BaseServiceImpl<CallInfoMapper, CallInfoDO> implements ICallInfoService {

    private final CallInfoDao callInfoDao;
    private final CallTransferInfoDao callTransferInfoDao;
    private final IPjSipService pjSipService;
    private final IQueueService queueService;

    @Override
    public Pg<CallInfoVO> page(CallInfoQueryVO callInfoQueryVO) {
        Pg<CallInfoVO> pageResult = callInfoDao.pgAs(Wrappers.getQueryWrapper()
                        .from(CallInfoDO.class).as("t")
                        .select(CALL_INFO_DO.DEFAULT_COLUMNS)
                        .select(CALL_ROUTE_DO.ROUTE_NAME.as(CallInfoVO::getIvrName))
                        .leftJoin(CallRouteDO.class).on(CallRouteDO::getId, CallInfoDO::getIvrId)
                        .eq(CallInfoDO::getType, callInfoQueryVO.getType(), Fc.notNull(callInfoQueryVO.getType()))
                        .eq(CallInfoDO::getExtension, callInfoQueryVO.getExtension(), Fc.isNotBlank(callInfoQueryVO.getExtension()))
                        .likeRight(CallInfoDO::getPhone, callInfoQueryVO.getPhone(), Fc.isNotBlank(callInfoQueryVO.getPhone()))
                        .eq(CallInfoDO::getChannelLine, callInfoQueryVO.getChannelLine(), Fc.isNotBlank(callInfoQueryVO.getChannelLine()))
                        .and(q->{
                            if (callInfoQueryVO.getState() >= CallHangStateEnum.NORMALLY.getValue()) {
                                q.eq(CallInfoDO::getHangupState, callInfoQueryVO.getState());
                            } else {
                                q.eq(CallInfoDO::getState, callInfoQueryVO.getState());
                            }
                        }, Fc.notNull(callInfoQueryVO.getState()))
                        .eq(CallInfoDO::getHangupUser, callInfoQueryVO.getHangupUser(), Fc.isNotBlank(callInfoQueryVO.getHangupUser()))
                        .and(q -> q.isNotNull(CallInfoDO::getAnswerTime).isNotNull(CallInfoDO::getHangupTime), Fc.notNull(callInfoQueryVO.getMinCallDuration()) || Fc.notNull(callInfoQueryVO.getMaxCallDuration()))
                        .ge("TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time)", callInfoQueryVO.getMinCallDuration(), Fc.notNull(callInfoQueryVO.getMinCallDuration()))
                        .le("TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time)", callInfoQueryVO.getMaxCallDuration(), Fc.notNull(callInfoQueryVO.getMaxCallDuration()))
                        .and(q -> {
                            q.isNull(CallInfoDO::getExtension)
                            .eq(CallInfoDO::getIsTransfer, Boolean.FALSE)
                                    .and(QueryMethods.notExists(QueryMethods.selectOne().from(CallTransferInfoDO.class).and(CallTransferInfoDO::getCallInfoId).eq(CallInfoDO::getId)));
                        }, callInfoQueryVO.getAi()).orderBy(CallInfoDO::getCreateTime).desc(),
                CallInfoVO.class);

        // 补全线路号码
        List<String> channelLines = pageResult.getList().stream().map(CallInfoVO::getChannelLine).distinct().filter(StrUtil::isNotBlank).toList();
        if (Fc.isNotEmpty(channelLines)) {
            List<EndpointsDO> endpointsDOS = pjSipService.list(Wrappers.getQueryWrapper().select(EndpointsDO::getId, EndpointsDO::getCallerid).in(EndpointsDO::getId, channelLines));
            Map<String, String> map = endpointsDOS.stream().collect(Collectors.toMap(EndpointsDO::getId, e -> StrUtil.emptyIfNull(e.getCallerid())));
            pageResult.getList().forEach(callInfoVO -> callInfoVO.setChannelCallId(map.get(callInfoVO.getChannelLine())));
        }
        return pageResult;
    }

    @Override
    public Pg<CallInfoAttendVO> callInfoAttend(CallInfoAttendQueryVO callInfoAttendQueryVO) {
        Pg<CallInfoAttendVO> pageResult = callInfoDao.pgAs(Wrappers.getQueryWrapper()
                        .from(CallInfoDO.class).as("t")
                        .select(CallInfoDO::getType, CallInfoDO::getOutType, CallInfoDO::getPhone, CallInfoDO::getChannelLine)
                        .select(CALL_INFO_DO.ID.as(CallInfoAttendVO::getCallInfoId),
                                CALL_TRANSFER_INFO_DO.ID.as(CallInfoAttendVO::getId),
                                CALL_TRANSFER_INFO_DO.QUEUE.as(CallInfoAttendVO::getQueue),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getExtension, CallInfoDO::getExtension).as(CallInfoAttendVO::getExtension),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getState, CallInfoDO::getState).as(CallInfoAttendVO::getState),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getHangupState, CallInfoDO::getHangupState).as(CallInfoAttendVO::getHangupState),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getHangupCause, CallInfoDO::getHangupCause).as(CallInfoAttendVO::getHangupCause),
                                QueryMethods.ifNull(CALL_TRANSFER_INFO_DO.HANGUP_EXT, QueryMethods.column("`t`.`hangup_user` != 'user'")).as(CallInfoAttendVO::getHangupExt),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getReadyTime, CallInfoDO::getCallTime).as(CallInfoAttendVO::getReadyTime),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getRingTime, CallInfoDO::getRingTime).as(CallInfoAttendVO::getRingTime),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getAnswerTime, CallInfoDO::getAnswerTime).as(CallInfoAttendVO::getAnswerTime),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getHangupTime, CallInfoDO::getHangupTime).as(CallInfoAttendVO::getHangupTime),
                                QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getFilePath, CallInfoDO::getFilePath).as(CallInfoAttendVO::getFilePath)
                        )
                        .leftJoin(CallTransferInfoDO.class).as("t1").on(CallTransferInfoDO::getCallInfoId, CallInfoDO::getId)
                        .eq(CallInfoDO::getType, callInfoAttendQueryVO.getType(), Fc.notNull(callInfoAttendQueryVO.getType()))
                        .likeRight(CallInfoDO::getPhone, callInfoAttendQueryVO.getPhone(), Fc.isNotBlank(callInfoAttendQueryVO.getPhone()))
                        .eq(CallInfoDO::getChannelLine, callInfoAttendQueryVO.getChannelLine(), Fc.isNotBlank(callInfoAttendQueryVO.getChannelLine()))
                        .eq(CallTransferInfoDO::getQueue, callInfoAttendQueryVO.getQueue(), Fc.isNotBlank(callInfoAttendQueryVO.getQueue()))
                        .and(andd->{
                            if (callInfoAttendQueryVO.getIsCallLoss()) {
                                // and (tran.stat = HANG or c.state = HANG) and (tran.hangupstate > NORMALLY or (tran.id is null and c.hangupstate > NORMALLY))
                                andd.and(q->{
                                    q.and(CallTransferInfoDO::getState).eq(CallStateEnum.HANG.getValue())
                                            .or(CallInfoDO::getState).eq(CallStateEnum.HANG.getValue());
                                })
                                .and(q->{
                                    q.gt(CallTransferInfoDO::getHangupState, CallHangStateEnum.NORMALLY.getValue()).or(or->{
                                        or.isNull(CallTransferInfoDO::getId).and(CallInfoDO::getHangupState).gt(CallHangStateEnum.NORMALLY.getValue());
                                    });
                                });
                            } else {
                                andd.isNotNull(CallInfoDO::getExtension).or(CallTransferInfoDO::getExtension).isNotNull();
                            }
                        })
                        .and(and->{
                            and.eq(CallInfoDO::getExtension, callInfoAttendQueryVO.getExtension())
                                .or(CallTransferInfoDO::getExtension)
                                .eq(callInfoAttendQueryVO.getExtension());
                        }, Fc.isNotBlank(callInfoAttendQueryVO.getExtension()))
                        .and(and->{
                            if (callInfoAttendQueryVO.getState() >= CallHangStateEnum.NORMALLY.getValue()) {
                                and.eq(CallTransferInfoDO::getHangupState, callInfoAttendQueryVO.getState()).or(or->{
                                    or.eq(CallInfoDO::getHangupState, callInfoAttendQueryVO.getState()).isNull(CallTransferInfoDO::getHangupState);
                                });
                            } else {
                                and.eq(CallTransferInfoDO::getState, callInfoAttendQueryVO.getState()).or(or->{
                                    or.eq(CallInfoDO::getState, callInfoAttendQueryVO.getState()).isNull(CallTransferInfoDO::getState);
                                });
                            }
                        }, Fc.notNull(callInfoAttendQueryVO.getState()))
                        .and(and -> {
                            and.or(or->{
                                or.isNotNull(CallInfoDO::getAnswerTime).isNotNull(CallInfoDO::getHangupTime);
                            }).or(or->{
                                or.isNotNull(CallTransferInfoDO::getAnswerTime).isNotNull(CallTransferInfoDO::getHangupTime);
                            });
                        }, Fc.notNull(callInfoAttendQueryVO.getMinCallDuration()) || Fc.notNull(callInfoAttendQueryVO.getMaxCallDuration()))
                        // (TIMESTAMPDIFF(SECOND, t1.answer_time, t1.hangup_time) >= {0} or ((t1.answer_time is null or t1.hangup_time is null) and TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time) >= {0}))
                        .and(and->{
                            and.ge("TIMESTAMPDIFF(SECOND, t1.answer_time, t1.hangup_time)", callInfoAttendQueryVO.getMinCallDuration())
                                .or(or->{
                                    or.or(o->{
                                        o.isNull(CallTransferInfoDO::getAnswerTime).or(CallTransferInfoDO::getHangupTime).isNull();
                                    })
                                    .and(a->{
                                        a.ge("TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time)", callInfoAttendQueryVO.getMinCallDuration());
                                    });
                                });
                        }, Fc.notNull(callInfoAttendQueryVO.getMinCallDuration()))
                        // (TIMESTAMPDIFF(SECOND, t1.answer_time, t1.hangup_time) <= {0} or ((t1.answer_time is null or t1.hangup_time is null) and TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time) <= {0}))
                        .and(and->{
                            and.le("TIMESTAMPDIFF(SECOND, t1.answer_time, t1.hangup_time)", callInfoAttendQueryVO.getMaxCallDuration())
                                    .or(or->{
                                        or.or(o->{
                                                    o.isNull(CallTransferInfoDO::getAnswerTime).or(CallTransferInfoDO::getHangupTime).isNull();
                                                })
                                                .and(a->{
                                                    a.le("TIMESTAMPDIFF(SECOND, t.answer_time, t.hangup_time)", callInfoAttendQueryVO.getMaxCallDuration());
                                                });
                                    });
                        }, Fc.notNull(callInfoAttendQueryVO.getMaxCallDuration()))
                        .orderBy(CallInfoDO::getCreateTime).desc(),
                CallInfoAttendVO.class
        );

        // 补全线路号码
        List<String> channelLines = pageResult.getList().stream().map(CallInfoAttendVO::getChannelLine).distinct().filter(StrUtil::isNotBlank).toList();
        if (Fc.isNotEmpty(channelLines)) {
            List<EndpointsDO> endpointsDOS = pjSipService.list(Wrappers.getQueryWrapper().select(EndpointsDO::getId, EndpointsDO::getCallerid).in(EndpointsDO::getId, channelLines));
            Map<String, String> map = endpointsDOS.stream().collect(Collectors.toMap(EndpointsDO::getId, e -> StrUtil.emptyIfNull(e.getCallerid())));
            pageResult.getList().forEach(callInfoAttendVO -> callInfoAttendVO.setChannelCallId(map.get(callInfoAttendVO.getChannelLine())));
        }
        // 补全队列名称
        List<String> queues = pageResult.getList().stream().map(CallInfoAttendVO::getQueue).distinct().filter(StrUtil::isNotBlank).toList();
        if (Fc.isNotEmpty(queues)) {
            List<QueuesDO> queuesDOS = queueService.list(Wrappers.getQueryWrapper().select(QueuesDO::getName, QueuesDO::getShowName).in(QueuesDO::getName, queues));
            Map<String, String> map = queuesDOS.stream().collect(Collectors.toMap(QueuesDO::getName, e -> StrUtil.emptyIfNull(e.getShowName())));
            pageResult.getList().forEach(callInfoAttendVO -> callInfoAttendVO.setQueue(map.getOrDefault(callInfoAttendVO.getQueue() ,callInfoAttendVO.getQueue())));
        }
        return pageResult;
    }

    @Override
    public Pg<TransferVO> transfer(TransferQueryVO transferQueryVO) {

        Pg<CallTransferInfoDO> pageResult = callTransferInfoDao.pg(Wrappers.getQueryWrapper()
                .eq(CallTransferInfoDO::getCallInfoId, transferQueryVO.getCallInfoId())
                .eq(CallTransferInfoDO::getExtension, transferQueryVO.getExtension(), Fc.isNotBlank(transferQueryVO.getExtension()))
                .eq(CallTransferInfoDO::getQueue, transferQueryVO.getQueue(), Fc.isNotBlank(transferQueryVO.getQueue()))
                .and(q->{
                    if (transferQueryVO.getState() >= CallHangStateEnum.NORMALLY.getValue()) {
                        q.eq(CallTransferInfoDO::getHangupState, transferQueryVO.getState());
                    } else {
                        q.eq(CallTransferInfoDO::getState, transferQueryVO.getState());
                    }
                }, Fc.notNull(transferQueryVO.getState()))
                .eq(CallTransferInfoDO::getHangupExt, transferQueryVO.getHangupExt(), Fc.notNull(transferQueryVO.getHangupExt()))
                .eq(CallTransferInfoDO::getHangupExt, transferQueryVO.getHangupExt(), Fc.notNull(transferQueryVO.getHangupExt()))
                .and(q -> q.isNotNull(CallTransferInfoDO::getAnswerTime).isNotNull(CallTransferInfoDO::getHangupTime), Fc.notNull(transferQueryVO.getMinCallDuration()) || Fc.notNull(transferQueryVO.getMaxCallDuration()))
                .ge("TIMESTAMPDIFF(SECOND, answer_time, hangup_time)", transferQueryVO.getMinCallDuration(), Fc.notNull(transferQueryVO.getMinCallDuration()))
                .le("TIMESTAMPDIFF(SECOND, answer_time, hangup_time)", transferQueryVO.getMaxCallDuration(), Fc.notNull(transferQueryVO.getMaxCallDuration()))
                .orderBy(CallTransferInfoDO::getCreateTime).desc());

        List<String> queues = pageResult.getList().stream().map(CallTransferInfoDO::getQueue).filter(StrUtil::isNotBlank).distinct().toList();
        AtomicReference<Map<String, String>> ref = new AtomicReference<>(new HashMap<>());
        if (Fc.isNotEmpty(queues)) {
            List<QueuesDO> queuesDOS = queueService.list(Wrappers.getQueryWrapper().select(QueuesDO::getName, QueuesDO::getShowName).in(QueuesDO::getName, queues));
            ref.set(queuesDOS.stream().collect(Collectors.toMap(QueuesDO::getName, e -> StrUtil.emptyIfNull(e.getShowName()))));
        }
        return Pg.of(pageResult.getTotal(), pageResult.getList().stream().map(t->{
            TransferVO transferVO = BeanUtil.copyProperties(t, TransferVO.class);
            transferVO.setQueueName(ref.get().getOrDefault(t.getQueue(), t.getQueue()));
            return transferVO;
        }).toList());
    }

    @Override
    public List<CallInfoByAttendPhoneVO> callInfoByAttendAndPhone(String attendId, String phone) {
        return callInfoDao.listAs(Wrappers.getQueryWrapper()
                .from(CallInfoDO.class).as("t")
                .select(CallInfoDO::getPhone, CallInfoDO::getCallTime, CallInfoDO::getType, CallInfoDO::getOutType, CallInfoDO::getChannelLine)
                .select(QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getRingTime, CallInfoDO::getRingTime).as(CallInfoByAttendPhoneVO::getRingTime),
                        QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getAnswerTime, CallInfoDO::getAnswerTime).as(CallInfoByAttendPhoneVO::getAnswerTime),
                        QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getHangupTime, CallInfoDO::getHangupTime).as(CallInfoByAttendPhoneVO::getHangupTime),
                        QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getState, CallInfoDO::getState).as(CallInfoByAttendPhoneVO::getState),
                        QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getHangupState, CallInfoDO::getHangupState).as(CallInfoByAttendPhoneVO::getHangupState),
                        QueryMethods.ifNull(CALL_TRANSFER_INFO_DO.HANGUP_EXT, QueryMethods.column("`t`.`hangup_user` != 'user'")).as(CallInfoAttendVO::getHangupExt),
                        QueryMethods.if_(CALL_TRANSFER_INFO_DO.ID.isNotNull(), CallTransferInfoDO::getFilePath, CallInfoDO::getFilePath).as(CallInfoByAttendPhoneVO::getFilePath))
                .leftJoin(CallTransferInfoDO.class).as( "t1").on(CallTransferInfoDO::getCallInfoId, CallInfoDO::getId)
                .eq(CallInfoDO::getPhone, phone)
                .and(and -> {
                    and.eq(CallInfoDO::getExtension, attendId)
                            .or(CallTransferInfoDO::getExtension)
                            .eq(attendId);
                })
                .orderBy(CallInfoDO::getCreateTime).desc(),
            CallInfoByAttendPhoneVO.class
        );
    }

    @Override
    public Flux<ServerSentEvent<Long>> botNum() {
        // 立即推送第一次，之后每三秒推送一次
        return Flux.interval(Duration.ZERO, Duration.ofSeconds(3))
                .flatMap(seq ->
                        Mono.fromCallable(() ->
                                callInfoDao.count(Wrappers.getQueryWrapper()
                                    .lt(CallInfoDO::getState, CallStateEnum.HANG.getValue())
                                    .isNull(CallInfoDO::getExtension)
                                    .eq(CallInfoDO::getIsTransfer, 0)
                                    .and(QueryMethods.notExists(QueryMethods.selectOne().from(CallTransferInfoDO.class).where(CallTransferInfoDO::getCallInfoId).eq(CallInfoDO::getId))))
                                )
                                .subscribeOn(Schedulers.boundedElastic())
                                .map(count -> ServerSentEvent.<Long>builder()
                                        .id(String.valueOf(seq))
                                        .data(count)
                                        .build()
                                )
                                .onErrorResume(Exception.class, e -> {
                                    log.error("SSE 查询异常", e);
                                    return Mono.empty();
                                })
                );
    }
}
