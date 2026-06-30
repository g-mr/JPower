package com.qidiangk.smart.aster.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.qidiangk.smart.aster.dbs.dao.asterisk.AuthsDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.*;
import com.qidiangk.smart.aster.pojo.vo.attend.*;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.DeviceStateChangeEvent;
import org.asteriskjava.manager.event.EndpointList;
import org.redisson.api.RedissonClient;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import top.jpower.core.asterisk.ami.service.AmiPJSipService;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.EndpointsTypeEnum;
import com.qidiangk.smart.aster.dbs.dao.asterisk.EndpointsDao;
import com.qidiangk.smart.aster.dbs.dao.cdr.CallInfoDao;
import com.qidiangk.smart.aster.dbs.entity.asterisk.*;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;
import com.qidiangk.smart.aster.pojo.vo.attend.*;
import com.qidiangk.smart.aster.pojo.vo.home.NameSelectVO;
import com.qidiangk.smart.aster.pojo.vo.ivr.QueueSelectVo;
import com.qidiangk.smart.aster.service.AttendService;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;
import com.qidiangk.smart.aster.service.asterisk.IQueueService;
import top.jpower.core.util.utils.StringUtil;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static com.qidiangk.smart.aster.constants.ConstantUtil.ONLINE_AGENT;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.AuthsDOTableDef.AUTHS_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.EndpointsDOTableDef.ENDPOINTS_DO;

@Slf4j
@Service
@RequiredArgsConstructor
public class AttendServiceImpl implements AttendService {

    private final EndpointsDao endpointsDao;
    private final AuthsDao authsDao;
    private final IPjSipService pjSipService;
    private final IQueueService queueService;
    private final AmiPJSipService amiPJSipService;
    private final RedissonClient redissonClient;
    private final CallInfoDao callInfoDao;

    @Override
    public boolean online(String memberName) {
        if (queueService.updatePaused(new GroupAttendStatusVO().setPaused(Boolean.FALSE).setMembername(memberName))) {
            // 发送事件
            DeviceStateChangeEvent event = new DeviceStateChangeEvent("");
            event.setPrivilege("call,all");
            event.setState("NOT_INUSE");
            event.setDevice(StrUtil.prependIfMissing(memberName, "PJSIP/"));
            event.setDateReceived(DateUtil.date());
            amiPJSipService.onManagerEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public boolean offline(String memberName) {
        if (queueService.updatePaused(new GroupAttendStatusVO().setPaused(Boolean.TRUE).setMembername(memberName))) {
            // 发送事件
            DeviceStateChangeEvent event = new DeviceStateChangeEvent("");
            event.setPrivilege("call,all");
            event.setState("PAUSED");
            event.setDevice(StrUtil.prependIfMissing(memberName, "PJSIP/"));
            event.setDateReceived(DateUtil.date());
            amiPJSipService.onManagerEvent(event);
            return true;
        }
        return false;
    }

    @Override
    public String create(AttendVO attendVO) {
        JpowerAssert.notTrue(endpointsDao.existEndpoint(attendVO.getId()), JpowerError.Business, "号码已存在");
        return pjSipService.createSip(attendVO);
    }

    @Override
    public String update(AttendVO attendVO) {
        return pjSipService.updateSip(attendVO);
    }

    @Override
    public boolean delete(String id) {
        // 删除坐席会员
        queueService.deleteMember("PJSIP/"+id);
        return pjSipService.delete(id);
    }

    @Override
    @SneakyThrows
    public Pg<AttendPageVO> page(AttendQueryVO attendQueryVO) {
        Pg<AttendPageVO> pageResult = pjSipService.page(attendQueryVO);
        if (Fc.isNotEmpty(pageResult.getList())){
            // 查询拨打状态
            Optional<List<EndpointList>> optional = amiPJSipService.queryEndpointList().blockOptional();
            Map<String, String> map = optional.map(endpointLists -> endpointLists.stream().collect(Collectors.toMap(EndpointList::getObjectName, EndpointList::getDevicestate))).orElseGet(HashMap::new);
            pageResult.getList().forEach(object -> {
                object.setDialState(map.get(object.getId()));
                object.setOnlineState(redissonClient.getMap(ONLINE_AGENT).containsKey(object.getId()));
            });
        }

        return pageResult;
    }

    @Override
    public String createGroup(GroupVO groupVO) {
        JpowerAssert.notTrue(queueService.existsByField(QueuesDO::getName, groupVO.getName()), JpowerError.Business, "坐席组已存在");
        QueuesDO queuesDO = BeanUtil.copyProperties(groupVO, QueuesDO.class);
        queueService.save(queuesDO);
        return queuesDO.getName();
    }

    @Override
    public Boolean updateGroup(GroupVO groupVO) {
        QueuesDO queuesDO = BeanUtil.copyProperties(groupVO, QueuesDO.class);
        return queueService.updateById(queuesDO);
    }

    @Override
    public Boolean deleteGroup(String name) {
        queueService.deleteMemberByQueue(name);
        return queueService.removeById(name);
    }

    @Override
    public Pg<GroupPageVO> pageGroup(GroupQueryVO groupQueryVO) {
        return queueService.page(groupQueryVO);
    }

    @Override
    public Boolean addAttend(AttendGroupSaveVO attendGroupSaveVO) {
        List<QueueMembersDO> list = new ArrayList<>();
        attendGroupSaveVO.getMemberIds().forEach(memberId -> {
            QueueMembersDO queueMembersDO = new QueueMembersDO();
            queueMembersDO.setQueueName(attendGroupSaveVO.getName());
            queueMembersDO.setInterfaceName("PJSIP/"+memberId);
            queueMembersDO.setMembername(memberId);
            queueMembersDO.setPaused(Boolean.FALSE);
            list.add(queueMembersDO);
        });
        return queueService.addAttend(list);
    }

    @Override
    public Boolean delAttend(String groupName, String membername) {
        return queueService.delAttend(groupName, membername);
    }

    @Override
    public List<GroupAttendVO> attendList(String groupName) {
        List<GroupAttendVO> list = queueService.attendList(groupName);
        if (Fc.isEmpty(list)){
            return ListUtil.empty();
        }
        List<String> membernames = list.stream().map(GroupAttendVO::getMembername).toList();
        List<AuthsDO> authsDOList = authsDao.listByIds(membernames);
        Map<String, String> authsDOMap = authsDOList.stream().collect(Collectors.toMap(AuthsDO::getId, AuthsDO::getUsername));
        return list.stream().peek(groupAttendVO -> {
            if (Fc.isNotEmpty(authsDOMap)){
                groupAttendVO.setUsername(authsDOMap.get(groupAttendVO.getMembername()));
            }
        }).toList();
    }

    @Override
    public Boolean updateAttendStatus(GroupAttendStatusVO groupAttendStatusVO) {
        boolean is = queueService.updatePaused(groupAttendStatusVO);

        if (queueService.isAllPaused(groupAttendStatusVO.getMembername())) {
            DeviceStateChangeEvent event = new DeviceStateChangeEvent("");
            event.setPrivilege("call,all");
            event.setState("PAUSED");
            event.setDevice(StrUtil.prependIfMissing(groupAttendStatusVO.getMembername(), "PJSIP/"));
            event.setDateReceived(DateUtil.date());
            amiPJSipService.onManagerEvent(event);
        } else {
            amiPJSipService.getEndpoint(groupAttendStatusVO.getMembername())
                    .switchIfEmpty(Mono.error(new JpowerException(404, "坐席状态未找到")))
                    .map(dto -> dto.getEndpoint().getDeviceState()).subscribe(state -> {
                        DeviceStateChangeEvent event = new DeviceStateChangeEvent("");
                        event.setPrivilege("call,all");
                        event.setState(state);
                        event.setDevice(StrUtil.prependIfMissing(groupAttendStatusVO.getMembername(), "PJSIP/"));
                        event.setDateReceived(DateUtil.date());
                        amiPJSipService.onManagerEvent(event);
                    });
        }

        return is;
    }

    @Override
    public List<String> attendOther(String groupName) {
        return pjSipService.objListAs(Wrappers.getQueryWrapper()
                        .select(EndpointsDO::getId)
                        .where(EndpointsDO::getBusinessType).eq(EndpointsTypeEnum.ATTEND.getName())
                        .and(QueryMethods.notExists(QueryMethods.selectOne().from(QueueMembersDO.class)
                                .where(QueueMembersDO::getQueueName).eq(groupName)
                                .and(QueueMembersDO::getMembername).eq(EndpointsDO::getId))),
                String.class);
    }

    @Override
    public List<NameSelectVO> getContactAttend() {
        return pjSipService.listAs(Wrappers.getQueryWrapper()
                        .select(ENDPOINTS_DO.ID.as(NameSelectVO::getName), AUTHS_DO.USERNAME.as(NameSelectVO::getShowName))
                        .leftJoin(AuthsDO.class).on(AuthsDO::getId, EndpointsDO::getAuth)
                        .innerJoin(ContactsDO.class).on(ContactsDO::getEndpoint, EndpointsDO::getId)
                        .gt(ContactsDO::getExpirationTime, DateUtil.currentSeconds())
                        .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName()),
                NameSelectVO.class);
    }

    @Override
    public Flux<ServerSentEvent<String>> getAttendStatus(String memberId) {
        // 先查一下状态
        Mono<String> currentStateMono = queueService.isAllPaused(memberId)
                ? Mono.just("PAUSED")
                : amiPJSipService.getEndpoint(memberId)
                .switchIfEmpty(Mono.error(new JpowerException(404, "坐席状态未找到")))
                .map(dto -> dto.getEndpoint().getDeviceState());


        // 再去订阅状态
        Flux<ServerSentEvent<String>> changeStream = amiPJSipService.subscribeEndpoint(memberId).map(deviceStateChangeEvent -> {

            if (StrUtil.equalsAnyIgnoreCase(deviceStateChangeEvent.getState(), "RINGING", "Ringing", "INUSE", "In use")) {
                // 查询正在通话的记录
                CallInfoDO callInfoDo = callInfoDao.getOne(Wrappers.getQueryWrapper()
                        .from(CallInfoDO.class).as("t")
                        .select(CallInfoDO::getType, CallInfoDO::getPhone, CallInfoDO::getChannelLine)
                        .leftJoin(CallTransferInfoDO.class).as("t1").on(CallTransferInfoDO::getCallInfoId, CallInfoDO::getId)
                        .and(and->{
                            and.and(CallInfoDO::getExtension).eq(memberId).or(CallTransferInfoDO::getExtension).eq(memberId);
                        })
                        .isNull(CallInfoDO::getHangupState)
                        .isNull(CallTransferInfoDO::getHangupState)
                        .orderBy(CallInfoDO::getCreateTime).desc().limit(1));

                if (Fc.notNull(callInfoDo)) {
                    Map<String, Object> map = MapUtil.<String, Object>builder()
                            .put("state", deviceStateChangeEvent.getState())
                            .put("callPhone", callInfoDo.getPhone())
                            .put("callLine", callInfoDo.getChannelLine())
                            .put("callType", callInfoDo.getType().getValue()).map();

                    return ServerSentEvent.builder(JSONUtil.toJsonStr(map)).build();
                }
            }

            return ServerSentEvent.builder(deviceStateChangeEvent.getState())
                                .build();
        });

        // 创建心跳流 (Heartbeat)
        Flux<ServerSentEvent<String>> heartbeatStream = Flux.interval(Duration.ofSeconds(15))
                .map(tick -> ServerSentEvent.<String>builder()
                        .comment("heartbeat")
                        .build()
                );

        return currentStateMono
                .map(state -> ServerSentEvent.builder(state).build())
                .flux()
                .concatWith(changeStream)
                .mergeWith(heartbeatStream);
    }

    @Override
    public List<String> selectAttend() {
        return pjSipService.objListAs(Wrappers.getQueryWrapper()
                .select(EndpointsDO::getId)
                .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName()),
            String.class);
    }

    @Override
    public boolean existsAttend(String id) {
        return pjSipService.exists(Wrappers.getQueryWrapper()
                .eq(EndpointsDO::getId, id)
                .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName()));
    }
}
