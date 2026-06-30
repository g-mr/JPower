package com.qidiangk.smart.aster.service.asterisk.impl;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.update.UpdateWrapper;
import com.qidiangk.smart.aster.constants.*;
import com.qidiangk.smart.aster.dbs.dao.asterisk.*;
import com.qidiangk.smart.aster.dbs.entity.asterisk.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.jpower.core.asterisk.properties.AsteriskProperties;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.aster.constants.*;
import com.qidiangk.smart.aster.dbs.dao.asterisk.*;
import com.qidiangk.smart.aster.dbs.dao.asterisk.mapper.EndpointsMapper;
import com.qidiangk.smart.aster.dbs.entity.asterisk.*;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendPageVO;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendQueryVO;
import com.qidiangk.smart.aster.pojo.vo.attend.AttendVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineBaseVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineQueryVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineUpdateVO;
import com.qidiangk.smart.aster.pojo.vo.line.LineVO;
import com.qidiangk.smart.aster.service.asterisk.IPjSipService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.AorsDOTableDef.AORS_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.AuthsDOTableDef.AUTHS_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.ContactsDOTableDef.CONTACTS_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.EndpointIdIpDOTableDef.ENDPOINT_ID_IP_DO;
import static com.qidiangk.smart.aster.dbs.entity.asterisk.table.EndpointsDOTableDef.ENDPOINTS_DO;

@Service
@RequiredArgsConstructor
public class PjSipServiceImpl extends BaseServiceImpl<EndpointsMapper, EndpointsDO> implements IPjSipService {

    private final AsteriskProperties asteriskProperties;
    private final EndpointsDao endpointsDao;
    private final AuthsDao authsDao;
    private final AorsDao aorsDao;
    private final RegistrationsDao registrationsDao;
    private final EndpointIdIpDao endpointIdIpDao;

    @Override
    @Transactional
    public String createSip(AttendVO attendVO) {
        // 保存AOR
        AorsDO aorsDO = AorsDO.builder()
                .id(attendVO.getId())
                .maxContacts(1)
                .qualifyFrequency(20)
                .qualifyTimeout(2.0)
                .removeUnavailable(Boolean.TRUE)
                .minimumExpiration(300)
                .maximumExpiration(1800)
                .removeExisting(Boolean.TRUE)
                .build();
        aorsDao.save(aorsDO);

        EndpointsDO endpointsDO = EndpointsDO.builder()
                .id(aorsDO.getId())
                .auth(aorsDO.getId())
                .aors(aorsDO.getId())
                .businessType(EndpointsTypeEnum.ATTEND.getName())
                .context(Optional.ofNullable(asteriskProperties.getContext().getOut()).orElseThrow(()->new JpowerException(500, "请先配置Context")))
                .transport(Optional.ofNullable(asteriskProperties.getTransport()).orElseThrow(()->new JpowerException(500, "请先配置Transport")))
                .disallow("all")
                .allow("ulaw,alaw,g729,gsm") // ulaw
                .directMedia(Boolean.FALSE)
                .forceRport(Boolean.TRUE)
                .rtpSymmetric(Boolean.TRUE)
                .rewriteContact(Boolean.TRUE)
                .dtmfMode(EndpointsDtmfModeEnum.RFC4733)
                .iceSupport(Boolean.FALSE)
                .useAvpf(Boolean.FALSE)
                .mediaEncryption(EndpointsMediaEncryptionEnum.NO)
                .callerid(attendVO.getUsername())
                .build();

        endpointsDao.save(endpointsDO);

        AuthsDO authsDO = AuthsDO.builder()
                .id(endpointsDO.getId())
                .authType(EndpointsAuthTypeEnum.USERPASS)
                .username(attendVO.getUsername())
                .password(attendVO.getPassword())
                .build();
        authsDao.save(authsDO);

        return endpointsDO.getId();
    }

    @Override
    @Transactional
    public String updateSip(AttendVO attendVO) {
        EndpointsDO endpoint = endpointsDao.getById(attendVO.getId());
        JpowerAssert.notNull(endpoint, JpowerError.NotFind, "坐席");
        endpoint.setContext(Optional.ofNullable(asteriskProperties.getContext().getOut()).orElseThrow(()->new JpowerException(500, "请先配置Context")));

        // 更新账号密码
        AuthsDO authsDO = authsDao.getById(endpoint.getAuth());
        JpowerAssert.notNull(authsDO, JpowerError.NotFind, "坐席");

        authsDO.setUsername(attendVO.getUsername());
        authsDO.setPassword(attendVO.getPassword());
        authsDao.updateById(authsDO);

        endpointsDao.updateById(endpoint);
        return endpoint.getId();
    }

    @Override
    @Transactional
    public boolean delete(String id) {
        EndpointsDO endpoint = endpointsDao.getById(id);
        JpowerAssert.notNull(endpoint, JpowerError.NotFind, "坐席");

        authsDao.removeById(endpoint.getAuth());
        aorsDao.removeById(endpoint.getAors());
        return endpointsDao.removeById(id);
    }

    @Override
    public Pg<AttendPageVO> page(AttendQueryVO attendQueryVO) {
        return endpointsDao.pgAs(Wrappers.getQueryWrapper()
                            .select(ENDPOINTS_DO.ID.as(AttendPageVO::getId),
                                    ENDPOINTS_DO.CONTEXT.as(AttendPageVO::getContext),
                                    ENDPOINTS_DO.CREATE_TIME.as(AttendPageVO::getCreateTime),
                                    AUTHS_DO.USERNAME.as(AttendPageVO::getUsername),
                                    AUTHS_DO.PASSWORD.as(AttendPageVO::getPassword),
                                    CONTACTS_DO.VIA_ADDR.as(AttendPageVO::getRegisterAddress),
                                    QueryMethods.column("UNIX_TIMESTAMP() < c.expiration_time and c.id is not null").as(AttendPageVO::getRegisterState))
                            .leftJoin(AuthsDO.class).on(AuthsDO::getId, EndpointsDO::getAuth)
                            .leftJoin(ContactsDO.class).as("c").on(ContactsDO::getEndpoint, EndpointsDO::getId)
                            .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.ATTEND.getName())
                            .likeRight(EndpointsDO::getId, attendQueryVO.getId(), Fc.isNotBlank(attendQueryVO.getId()))
                            .likeRight(AuthsDO::getUsername, attendQueryVO.getUsername(), Fc.isNotBlank(attendQueryVO.getUsername()))
                            .where(ContactsDO::getExpirationTime).gt(QueryMethods.unixTimestamp(), Fc.toBoolean(attendQueryVO.getRegisterState(), false))
                            .and(and -> and.isNull(ContactsDO::getId).or(ContactsDO::getExpirationTime).lt(QueryMethods.unixTimestamp()), Fc.equalsValue(attendQueryVO.getRegisterState(), false))
                            .orderBy(ENDPOINTS_DO.CREATE_TIME.desc())
                            , AttendPageVO.class);
    }

    @Override
    public List<AuthsDO> listUsernameByAuthId(List<String> memberIds) {
        return authsDao.listByIds(memberIds);
    }

    @Override
    @Transactional
    public String createLine(LineBaseVO lineBaseVO) {
        // 保存AOR
        AorsDO aorsDO = AorsDO.builder()
                .id(lineBaseVO.getCallerid())
                .maxContacts(lineBaseVO.getMaxContacts())
                .contact("sip:"+ lineBaseVO.getIp()+":"+ lineBaseVO.getPort())
                .build();
        aorsDao.save(aorsDO);


        EndpointsDO endpointsDO = EndpointsDO.builder()
                .id(aorsDO.getId())
                .aors(aorsDO.getId())
                .businessType(EndpointsTypeEnum.LINE.getName())
                .context(Optional.ofNullable(asteriskProperties.getContext().getIn()).orElseThrow(()->new JpowerException(500, "请先配置Context")))
                .transport(Optional.ofNullable(asteriskProperties.getTransport()).orElseThrow(()->new JpowerException(500, "请先配置Transport")))
                .disallow("all")
                .allow("ulaw,alaw,g729,gsm")
                .addZero(lineBaseVO.getIsAddZero())
                .prefix(lineBaseVO.getPrefix())
                .trustIdInbound(Boolean.TRUE)
                .trustIdOutbound(Boolean.TRUE)
                .suppressMohOnSendonly(Boolean.TRUE)
                .inbandProgress(Boolean.FALSE)
                .usePtime(Boolean.FALSE)
                .rtpSymmetric(Boolean.TRUE)
                .directMedia(Boolean.FALSE)
                .mediaEncryption(EndpointsMediaEncryptionEnum.NO)
                .iceSupport(Boolean.FALSE)
                .timers(Boolean.FALSE)
                .forceRport(Boolean.TRUE)
                .rewriteContact(Boolean.TRUE)
                .dtmfMode(EndpointsDtmfModeEnum.RFC4733)
                .useAvpf(Boolean.FALSE)
                .callerid(lineBaseVO.getCallerid())
                .build();

        if (Fc.notNull(lineBaseVO.getRouteId())) {
            endpointsDO.setSetVar(VariableNameEnum.ROUTE_ID.getName()+"="+lineBaseVO.getRouteId());
        }


        // 请求认证
        if (lineBaseVO.getIsAuth()) {
            AuthsDO authsDO = AuthsDO.builder()
                    .id(IdUtil.getSnowflakeNextIdStr())
                    .authType(EndpointsAuthTypeEnum.USERPASS)
                    .username(lineBaseVO.getAuthUsername())
                    .password(lineBaseVO.getAuthPassword())
                    .build();
            authsDao.save(authsDO);
            endpointsDO.setAuth(authsDO.getId());
        }

        // 主动注册
        if (lineBaseVO.getIsRegister()) {
            AuthsDO authsDO = AuthsDO.builder()
                    .id(aorsDO.getId())
                    .authType(EndpointsAuthTypeEnum.USERPASS)
                    .username(lineBaseVO.getUsername())
                    .password(lineBaseVO.getPassword())
                    .build();
            authsDao.save(authsDO);
            endpointsDO.setOutboundAuth(authsDO.getId());

            RegistrationsDO registrationsDO = RegistrationsDO.builder()
                    .id(aorsDO.getId())
                    .transport(Optional.ofNullable(asteriskProperties.getTransport()).orElseThrow(()->new JpowerException(500, "请先配置Transport")))
                    .outboundAuth(authsDO.getId())
                    .serverUri("sip:"+ lineBaseVO.getIp()+":"+ lineBaseVO.getPort())
                    .clientUri("sip:"+ lineBaseVO.getCallerid()+"@"+ lineBaseVO.getIp()+":"+ lineBaseVO.getPort())
                    .contactUser(lineBaseVO.getCallerid())
                    .build();
            registrationsDao.save(registrationsDO);
        }
        endpointsDao.save(endpointsDO);

        // 来源匹配
        EndpointIdIpDO endpointIdIpDO = EndpointIdIpDO.builder()
                .id(aorsDO.getId())
                .endpoint(endpointsDO.getId())
                .match(lineBaseVO.getIp())
                .srvLookups(lineBaseVO.getSrvLookups())
                .build();
        endpointIdIpDao.save(endpointIdIpDO);

        return endpointsDO.getId();
    }

    @Override
    @Transactional
    public boolean removeLineById(String id) {
        EndpointsDO endpointsDO = endpointsDao.getById(id);
        if (Fc.isNotBlank(endpointsDO.getAuth())) {
            authsDao.removeById(endpointsDO.getAuth());
        }
        aorsDao.removeById(endpointsDO.getAors());
        if (Fc.isNotBlank(endpointsDO.getOutboundAuth())) {
            authsDao.removeById(endpointsDO.getOutboundAuth());
        }
        endpointIdIpDao.removeById(id);
        registrationsDao.removeById(id);
        return endpointsDao.removeById(id);
    }

    @Override
    @Transactional
    public boolean updateLineById(LineUpdateVO lineVO) {
        // 保存AOR
        AorsDO aorsDO = AorsDO.builder()
                .id(lineVO.getId())
                .maxContacts(lineVO.getMaxContacts())
                .contact("sip:"+lineVO.getIp()+":"+lineVO.getPort())
                .build();
        aorsDao.updateById(aorsDO);


        EndpointsDO endpointsDO = endpointsDao.getById(lineVO.getId());
        endpointsDO.setAddZero(lineVO.getIsAddZero())
                .setPrefix(lineVO.getPrefix())
                .setCallerid(lineVO.getCallerid())
                .setOutboundAuth(null);

        if (Fc.notNull(lineVO.getRouteId())) {
            endpointsDO.setSetVar(VariableNameEnum.ROUTE_ID.getName()+"="+lineVO.getRouteId());
        } else {
            endpointsDO.setSetVar(null);
        }

        // 请求认证
        if (lineVO.getIsAuth()) {
            AuthsDO authsDO = Fc.isBlank(endpointsDO.getAuth()) ? null : authsDao.getById(endpointsDO.getAuth());
            if (authsDO != null){
                authsDO.setUsername(lineVO.getAuthUsername())
                        .setPassword(lineVO.getAuthPassword());
                authsDao.updateById(authsDO);
            } else {
                authsDO = AuthsDO.builder()
                        .id(IdUtil.getSnowflakeNextIdStr())
                        .authType(EndpointsAuthTypeEnum.USERPASS)
                        .username(lineVO.getAuthUsername())
                        .password(lineVO.getAuthPassword())
                        .build();
                authsDao.save(authsDO);
                endpointsDO.setAuth(authsDO.getId());
            }
        } else {
            if (Fc.isNotBlank(endpointsDO.getAuth())) {
                authsDao.removeById(endpointsDO.getAuth());
            }
            endpointsDO.setAuth(null);
        }

        // 主动注册
        if (lineVO.getIsRegister()) {
            RegistrationsDO registrationsDO = registrationsDao.getById(aorsDO.getId());
            if (registrationsDO != null) {
                registrationsDO.setServerUri("sip:"+lineVO.getIp()+":"+lineVO.getPort())
                        .setClientUri("sip:"+lineVO.getCallerid()+"@"+lineVO.getIp()+":"+lineVO.getPort())
                        .setContactUser(lineVO.getCallerid());
                registrationsDao.updateById(registrationsDO);

                authsDao.update(UpdateWrapper.of(AuthsDO.class)
                        .set(AuthsDO::getUsername, lineVO.getUsername())
                        .set(AuthsDO::getPassword, lineVO.getPassword()).toEntity(),
                        Wrappers.getQueryWrapper()
                        .eq(AuthsDO::getId, registrationsDO.getOutboundAuth()));
            } else {
                AuthsDO authsDO = AuthsDO.builder()
                        .id(aorsDO.getId())
                        .authType(EndpointsAuthTypeEnum.USERPASS)
                        .username(lineVO.getUsername())
                        .password(lineVO.getPassword())
                        .build();
                authsDao.save(authsDO);

                registrationsDO = RegistrationsDO.builder()
                        .id(aorsDO.getId())
                        .transport(Optional.ofNullable(asteriskProperties.getTransport()).orElseThrow(()->new JpowerException(500, "请先配置Transport")))
                        .outboundAuth(authsDO.getId())
                        .serverUri("sip:"+lineVO.getIp()+":"+lineVO.getPort())
                        .clientUri("sip:"+lineVO.getCallerid()+"@"+lineVO.getIp()+":"+lineVO.getPort())
                        .contactUser(lineVO.getCallerid())
                        .build();
                registrationsDao.save(registrationsDO);
            }
            endpointsDO.setOutboundAuth(registrationsDO.getOutboundAuth());
        } else {
            RegistrationsDO registrationsDO = registrationsDao.getById(aorsDO.getId());
            if (registrationsDO != null) {
                authsDao.removeById(registrationsDO.getOutboundAuth());
                registrationsDao.removeById(registrationsDO.getId());
            }
        }

        // 来源匹配
        EndpointIdIpDO endpointIdIpDO = EndpointIdIpDO.builder()
                .id(aorsDO.getId())
                .endpoint(endpointsDO.getId())
                .match(lineVO.getIp())
                .srvLookups(lineVO.getSrvLookups())
                .build();
        endpointIdIpDao.updateById(endpointIdIpDO);

        return endpointsDao.updateAllById(endpointsDO);
    }

    @Override
    public Pg<LineVO> pageLine(LineQueryVO lineQueryVO) {
        var a = AUTHS_DO.as("a");
        var a1 = AUTHS_DO.as("a1");

        Pg<LineVO> pageResult = endpointsDao.pgAs(
                Wrappers.getQueryWrapper()
                        .select(ENDPOINTS_DO.ID.as(LineVO::getId),
                                ENDPOINTS_DO.CALLERID.as(LineVO::getCallerid),
                                ENDPOINTS_DO.ADD_ZERO.as(LineVO::getIsAddZero),
                                ENDPOINTS_DO.PREFIX.as(LineVO::getPrefix),
                                ENDPOINTS_DO.SET_VAR.as(LineVO::getVars),
                                ENDPOINTS_DO.CREATE_TIME.as(LineVO::getCreateTime),
                                ENDPOINT_ID_IP_DO.SRV_LOOKUPS.as(LineVO::getSrvLookups),
                                AORS_DO.MAX_CONTACTS.as(LineVO::getMaxContacts),
                                AORS_DO.CONTACT.as(LineVO::getIp),
                                QueryMethods.if_(ENDPOINTS_DO.AUTH.isNotNull(), QueryMethods.column("1"), QueryMethods.column("0")).as(LineVO::getIsAuth),
                                QueryMethods.if_(ENDPOINTS_DO.OUTBOUND_AUTH.isNotNull(), QueryMethods.column("1"), QueryMethods.column("0")).as(LineVO::getIsRegister),
                                a.USERNAME.as(LineVO::getAuthUsername),
                                a.PASSWORD.as(LineVO::getAuthPassword),
                                a1.USERNAME.as(LineVO::getUsername),
                                a1.PASSWORD.as(LineVO::getPassword))
                        .leftJoin(AorsDO.class).on(AorsDO::getId, EndpointsDO::getAors)
                        .leftJoin(a).on(a.ID.eq(ENDPOINTS_DO.AUTH))
                        .leftJoin(a1).on(a1.ID.eq(ENDPOINTS_DO.OUTBOUND_AUTH))
                        .leftJoin(EndpointIdIpDO.class).on(EndpointIdIpDO::getEndpoint, EndpointsDO::getId)
                        .eq(EndpointsDO::getBusinessType, EndpointsTypeEnum.LINE.getName())
                        .likeRight(EndpointsDO::getCallerid, lineQueryVO.getCallerid(), Fc.isNotBlank(lineQueryVO.getCallerid()))
                        .isNotNull(EndpointsDO::getOutboundAuth, Fc.notNull(lineQueryVO.getIsRegister()) && lineQueryVO.getIsRegister())
                        .isNull(EndpointsDO::getOutboundAuth, Fc.notNull(lineQueryVO.getIsRegister()) && !lineQueryVO.getIsRegister())
                        .isNotNull(EndpointsDO::getAuth, Fc.notNull(lineQueryVO.getIsAuth()) && lineQueryVO.getIsAuth())
                        .isNull(EndpointsDO::getAuth, Fc.notNull(lineQueryVO.getIsAuth()) && !lineQueryVO.getIsAuth())
                        .like(AorsDO::getContact, lineQueryVO.getIp(), Fc.isNotBlank(lineQueryVO.getIp()))
                        .orderBy(EndpointsDO::getCreateTime).desc()
                    , LineVO.class);

        pageResult.getList().forEach(lineUpdateVO -> {
            if (Fc.isNotBlank(lineUpdateVO.getIp())) {
                List<String> list = StrUtil.split(lineUpdateVO.getIp(), ":");
                lineUpdateVO.setIp(list.get(1));
                lineUpdateVO.setPort(list.get(2));
            }
            if (Fc.isNotBlank(lineUpdateVO.getVars())) {
                Map<String, String> map = StrUtil.split(lineUpdateVO.getVars(), StrUtil.LF).stream().collect(Collectors.toMap(str->{
                    return StrUtil.split(str, "=").get(0);
                },str->StrUtil.split(str, "=").get(1)));
                lineUpdateVO.setRouteId(MapUtil.getLong(map, VariableNameEnum.ROUTE_ID.getName()));
            }
        });

        return pageResult;
    }

    @Override
    public String getLimit1Line() {
        return endpointsDao.getLimit1Line();
    }

}
