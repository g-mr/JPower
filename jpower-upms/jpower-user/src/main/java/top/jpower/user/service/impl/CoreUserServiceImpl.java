package top.jpower.user.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import top.jpower.common.constants.CacheNames;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.constants.ParamsConstants;
import top.jpower.common.enums.IdTypeEnum;
import top.jpower.common.enums.UserTypeEnum;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.dbs.tenant.JpowerTenantProperties;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.DigestUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MD5;
import top.jpower.core.util.utils.UuidUtil;
import top.jpower.system.api.cache.SystemCache;
import top.jpower.system.api.cache.param.ParamCache;
import top.jpower.system.api.dto.TenantDTO;
import top.jpower.user.dbs.dao.CoreUserDao;
import top.jpower.user.dbs.dao.CoreUserRoleDao;
import top.jpower.user.dbs.dao.mapper.CoreUserMapper;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.dbs.entity.CoreUserRole;
import top.jpower.user.service.CoreUserService;
import top.jpower.user.vo.LoginUserVO;
import top.jpower.user.vo.UserVO;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static top.jpower.common.constants.CacheNames.TOKEN_USER_KEY;
import static top.jpower.common.constants.ServiceCodeConstants.*;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.core.dbs.tenant.TenantConstant.TENANT_ACCOUNT_NUMBER;
import static top.jpower.core.dbs.tenant.TenantConstant.getAccountNumber;

/**
 * 用户服务
 *
 * @author mr.gmac
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CoreUserServiceImpl extends BaseServiceImpl<CoreUserMapper, CoreUser> implements CoreUserService {

    private final CoreUserDao coreUserDao;
    private final CoreUserRoleDao coreUserRoleDao;
    private final RedisService redisService;
    private final JpowerTenantProperties tenantProperties;

    @Override
    public Pg<UserVO> listPage(CoreUser coreUser) {
        Pg<UserVO> userVo = coreUserDao.pageVO(coreUser);
        //查询用户在线信息
        userVo.getList().forEach(user-> user.setOnLine(redisService.keys(TOKEN_USER_KEY+user.getId() + StringPool.COLON + StringPool.ASTERISK).size()));
        return userVo;
    }

    @Override
    public List<UserVO> list(CoreUser coreUser) {
        return coreUserDao.listVO(coreUser);
    }

    @Override
    public boolean save(CoreUser coreUser) {
        setActivationStatus(coreUser);
		if (Fc.isBlank(coreUser.getPassword())) {
			coreUser.setPassword(DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamCache.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD))));
		}
        return coreUserDao.save(coreUser);
    }

    private void setActivationStatus(CoreUser coreUser) {
        if (Fc.isNull(coreUser.getActivationStatus())){
            Integer isActivation = ParamCache.getInt(ParamsConstants.IS_ACTIVATION, DefaultValConstants.DEFAULT_USER_ACTIVATION);
            coreUser.setActivationStatus(Fc.toBool(isActivation));
        }

        if (!coreUser.getActivationStatus()){
            coreUser.setActivationCode(UuidUtil.create10UUidNum());
            coreUser.setActivationStatus(Boolean.FALSE);
        }
    }


    @Override
    public Boolean deleteByIds(List<Long> ids) {
        ids = new ArrayList<>(ids);

        if(Fc.contains(ids, RoleConstant.ROOT_ID) || Fc.contains(ids, RoleConstant.ANONYMOUS_ID)){
            ids.removeIf(obj -> NumberUtil.equals(obj,RoleConstant.ROOT_ID) || NumberUtil.equals(obj,RoleConstant.ANONYMOUS_ID));
            JpowerAssert.notGeZero(ids.size(), JpowerError.Business, USER_NOT_DELETE);
        }

        boolean is = coreUserDao.removeByIds(ids);
        if (is){
            return coreUserRoleDao.deleteByUserIds(ids);
        }
        return false;
    }

    @Override
    public Boolean updateUser(@Validated(Validation.Update.class) CoreUser coreUser) {
        if (Fc.notNull(coreUser.getIdType()) && IdTypeEnum.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !Validator.isCitizenId(coreUser.getIdNo())) {
                JpowerAssert.createException(JpowerError.Business, IDCARD_NOT_LEGAL);
            }
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())) {
            CoreUser user = this.selectUserLoginId(coreUser.getLoginId(), coreUser.getTenantCode());
            if (user != null && !NumberUtil.equals(user.getId(), coreUser.getId())) {
                JpowerAssert.createException(JpowerError.Business, LOGIN_ID_EXISTS);
            }
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())) {
            CoreUser user = this.selectByPhone(coreUser.getTelephone(), coreUser.getTenantCode());
            if (user != null && !NumberUtil.equals(user.getId(), coreUser.getId())) {
                JpowerAssert.createException(JpowerError.Business, MOBILE_EXISTS);
            }
        }

        CoreUser user = coreUserDao.getById(coreUser.getId());
        JpowerAssert.notNull(user, JpowerError.NotFind,NOT_FOUND_USER);

        // 不能修改密码和租户
        coreUser.setPassword(null);
        coreUser.setTenantCode(null);

        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserDao.updateById(coreUser);
    }

    @Override
    public CoreUser selectUserLoginId(String loginId,String tenantCode) {
        return coreUserDao.getOneByField(CoreUser::getLoginId,loginId);
    }

    @Override
    public boolean validatePassword(String account, String password, String tenantCode) {
        String userPassword = coreUserDao.getPassword(account);
        return DigestUtil.checkPwd(password, userPassword);
    }

    @Override
    public UserVO selectUserById(Long id) {
        return coreUserDao.selectAllById(id);
    }

    @Override
    public CoreUser selectUserByOtherCode(String otherCode, String tenantCode) {
        return coreUserDao.getOneByField(CoreUser::getOtherCode, otherCode);
    }

    @Override
    public Long saveUser(CoreUser user, List<Long> roleIds) {
        if (coreUserDao.save(user)){
            if (Fc.isNotEmpty(roleIds)) {
                List<CoreUserRole> userRoleList = new ArrayList<>();
                roleIds.forEach(roleId->{
                    CoreUserRole userRole = new CoreUserRole();
                    userRole.setUserId(user.getId());
                    userRole.setRoleId(roleId);
                    userRoleList.add(userRole);
                });
                if (Fc.isNotEmpty(userRoleList)){
                    coreUserRoleDao.saveBatch(userRoleList);
                }
            }
            return user.getId();
        }
        return null;
    }

    @Override
    public boolean resetPassword(List<Long> ids) {
        String pass = DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamCache.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD)));
        return coreUserDao.updatePassword(pass, ids);
    }

    /**
     * @author 郭丁志
     * @Description //TODO 去除重复以及正确验证
     * @date 1:46 2020/10/20 0020
     */
    private <T> Predicate<T> filterUser(Function<? super T, CoreUser> keyExtractor) {
        Map<Object, Boolean> loginIdMap = new ConcurrentHashMap<>(16);
        Map<Object, Boolean> phoneMap = new ConcurrentHashMap<>(16);

        return object ->
            Fc.isNull(loginIdMap.putIfAbsent(keyExtractor.apply(object).getLoginId(), Boolean.TRUE))&&
                    Fc.isNotBlank(keyExtractor.apply(object).getTelephone()) ? Fc.isNull(phoneMap.putIfAbsent(keyExtractor.apply(object).getTelephone(), Boolean.TRUE)) : Boolean.TRUE;
    }

    @Override
    public boolean insertBatch(List<CoreUser> list,boolean isCover) {

        List<CoreUser> userList = new ArrayList<>();

        String password = DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamCache.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD)));

        for (CoreUser coreUser : list) {
            if (Fc.isBlank(coreUser.getLoginId())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getTelephone()) && !Validator.isMobile(coreUser.getTelephone())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getEmail()) && !Validator.isEmail(coreUser.getEmail())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getIdNo()) && IdTypeEnum.ID_CARD.getValue().equals(coreUser.getIdType()) && !Validator.isCitizenId(coreUser.getIdNo())){
                continue;
            }

            coreUser.setPassword(password);
            coreUser.setUserType(UserTypeEnum.USER_TYPE_SYSTEM.getValue());

            if (ShieldUtil.isRoot()){
                coreUser.setTenantCode(Fc.isBlank(coreUser.getTenantCode())? Fc.toStr(ShieldUtil.getTenantCode(),DEFAULT_TENANT_CODE):coreUser.getTenantCode());
            }else {
                coreUser.setTenantCode(Fc.toStr(ShieldUtil.getTenantCode(), DEFAULT_TENANT_CODE));
            }

            setActivationStatus(coreUser);

            CoreUser user = this.selectUserLoginId(coreUser.getLoginId(), coreUser.getTenantCode());
            if (Fc.notNull(user)){
                if (isCover){
                    coreUser.setId(user.getId());
                }else {
                    //如果loginID重复但是不进行覆盖则去除
                    continue;
                }
            }

            if (Fc.isNotBlank(coreUser.getTelephone())){
                user = this.selectByPhone(coreUser.getTelephone(),coreUser.getTenantCode());
                if (Fc.notNull(user)) {
                    if (isCover) {
                        if (Fc.notNull(coreUser.getId()) && !NumberUtil.equals(coreUser.getId(),user.getId())){
                            //如果loginID已经重复且不是一条数据的情况下，不进行覆盖也不新增
                            continue;
                        }
                        coreUser.setId(user.getId());
                    }else {
                        //如果手机号重复但是不进行覆盖则去除
                        continue;
                    }
                }
            }

            userList.add(coreUser);
        }

        //list去重
        userList = userList.stream().filter(filterUser(o -> o)).collect(Collectors.toList());

        List<String> tenantCodes = userList.stream().map(CoreUser::getTenantCode).distinct().toList();
        tenantCodes.forEach(tenantCode -> {

            TenantDTO tenant = SystemCache.getTenantByCode(tenantCode);
            if (Fc.isNull(tenant)){
                throw new BusinessException(tenantCode+TENANT_NOT_EXIST);
            }
            long accountNumber = getAccountNumber(tenant.getLicenseKey());
            if (!Fc.equals(accountNumber, TENANT_ACCOUNT_NUMBER)){
                long count = coreUserDao.countByTenant(tenantCode);
                if (!NumberUtil.equals(accountNumber,-1L) && count >= accountNumber){
                    throw new BusinessException(tenant.getTenantName()+ACCOUNT_LIMIT);
                }
            }

        });

        return coreUserDao.saveOrUpdateBatch(userList);
    }

    @Override
    public Boolean updateUsersRole(List<Long> userIds, List<Long> roleIds) {
        coreUserRoleDao.deleteByUserIds(userIds);
        // 清除缓存
        CacheUtil.clear(CacheNames.USER_KEY);

        List<CoreUserRole> userRoles = new ArrayList<>();
        if (Fc.isNotEmpty(roleIds)) {
            userRoles = roleIds.stream()
                    .flatMap(roleId -> userIds.stream()
                            .map(userId -> {
                                CoreUserRole userRole = new CoreUserRole();
                                userRole.setRoleId(roleId);
                                userRole.setUserId(userId);
                                return userRole;
                            }))
                    .collect(Collectors.toList());
        }

        //如果修改超级用户，并且角色不包含超级用户角色，则给超级用户添加超级用户角色
        if (Fc.contains(userIds,RoleConstant.ROOT_ID) && !Fc.contains(roleIds,RoleConstant.ROOT_ID)){
            CoreUserRole userRole = new CoreUserRole();
            userRole.setUserId(RoleConstant.ROOT_ID);
            userRole.setRoleId(RoleConstant.ROOT_ID);
            userRoles.add(userRole);
        }

        //如果修改匿名用户，并且角色不包含匿名用户角色，则给匿名用户添加匿名用户角色
        if (Fc.contains(userIds,RoleConstant.ANONYMOUS_ID) && !Fc.contains(roleIds,RoleConstant.ANONYMOUS_ID)){
            CoreUserRole userRole = new CoreUserRole();
            userRole.setUserId(RoleConstant.ANONYMOUS_ID);
            userRole.setRoleId(RoleConstant.ANONYMOUS_ID);
            userRoles.add(userRole);
        }

        if (userRoles.size() > 0){
            return coreUserRoleDao.saveBatch(userRoles, userRoles.size());
        }

        return true;
    }

    @Override
    public boolean addRoleUsers(Long roleId, List<Long> userIds) {
        List<CoreUserRole> list = new ArrayList<>();
        userIds.forEach((userId)-> list.add(new CoreUserRole()
                .setRoleId(roleId)
                .setUserId(userId)));
        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserRoleDao.saveBatch(list);
    }

    @Override
    public boolean deleteRoleUsers(Long roleId, List<Long> userIds) {
        userIds.removeIf(userId->
                (Fc.equalsValue(roleId, RoleConstant.ROOT_ID) && Fc.equalsValue(userId,RoleConstant.ROOT_ID))
                ||
                (Fc.equalsValue(roleId, RoleConstant.ANONYMOUS_ID) && Fc.equalsValue(userId,RoleConstant.ANONYMOUS_ID)));

        JpowerAssert.notEmpty(userIds, JpowerError.Business, USER_ROLE_NOT_DELETE);

        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserRoleDao.deleteByRoleAndUserIds(roleId, userIds);
    }

    @Override
    public CoreUser selectByPhone(String phone,String tenantCode) {
        return coreUserDao.getOneByField(CoreUser::getTelephone, phone);
    }

    @Override
    public Boolean updateLoginCount(Long id) {
        return coreUserDao.updateLoginCount(id);
    }

    /**
     * 修改用户手机号
     *
     * @author mr.g
     * @param userId 用户ID
     * @param phone 要修改的手机号
     * @return 是否成功
     **/
    @Override
    public boolean updatePhone(String phone, Long userId) {
        boolean is = coreUserDao.exists(Wrappers.getQueryWrapper().eq(CoreUser::getTelephone, phone));
        JpowerAssert.notTrue(is, JpowerError.Business, MOBILE_BINGING);

        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserDao.updatePhone(userId, phone);
    }

    @Override
    public boolean updateEmail(String email, Long userId) {
        boolean is = coreUserDao.exists(Wrappers.getQueryWrapper().eq(CoreUser::getEmail, email));
        JpowerAssert.notTrue(is, JpowerError.Business, EMAIL_BINGING);

        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserDao.updateEmail(userId, email);
    }

    @Override
    public boolean createUser(CoreUser coreUser) {

        if (coreUser.getIdType() != null && IdTypeEnum.ID_CARD.getValue().equals(coreUser.getIdType())) {
            if (Fc.isNotBlank(coreUser.getIdNo()) && !Validator.isCitizenId(coreUser.getIdNo())) {
                JpowerAssert.createException(JpowerError.Business, IDCARD_NOT_LEGAL);
            }
        }

        String tenantCode = Fc.toStr(coreUser.getTenantCode(), ShieldUtil.getTenantCode());
        if (tenantProperties.getEnable()) {
            if (ShieldUtil.isRoot()) {
                tenantCode = Fc.isBlank(coreUser.getTenantCode()) ? DEFAULT_TENANT_CODE : coreUser.getTenantCode();
            }
            TenantDTO tenant = SystemCache.getTenantByCode(tenantCode);
            if (Fc.isNull(tenant)) {
                JpowerAssert.createException(JpowerError.NotFind, TENANT_NOT_EXIST);
                long accountNumber = getAccountNumber(tenant.getLicenseKey());
                if (!Fc.equalsValue(accountNumber, TENANT_ACCOUNT_NUMBER)) {
                    long count = coreUserDao.countByTenant(tenantCode);
                    if (count >= accountNumber) {
                        JpowerAssert.createException(JpowerError.NotFind, ACCOUNT_LIMIT);
                    }
                }
            }

            if (StringUtils.isNotBlank(coreUser.getTelephone())) {
                JpowerAssert.isNull(this.selectByPhone(coreUser.getTelephone(), tenantCode), JpowerError.Business, MOBILE_EXISTS);
            }
            JpowerAssert.isNull(this.selectUserLoginId(coreUser.getLoginId(), tenantCode), JpowerError.Business, LOGIN_ID_EXISTS);

            coreUser.setPassword(DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamCache.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD))));
            if (Fc.isNull(coreUser.getUserType())) {
                coreUser.setUserType(UserTypeEnum.USER_TYPE_SYSTEM.getValue());
            }
            CacheUtil.clear(CacheNames.USER_KEY);
        }
        return super.save(coreUser);
    }

    @Override
    public boolean updateUserInfo(LoginUserVO userVO) {
        if (userVO.getIdType() != null && IdTypeEnum.ID_CARD.getValue().equals(userVO.getIdType())) {
            if (Fc.isNotBlank(userVO.getIdNo()) && !Validator.isCitizenId(userVO.getIdNo())) {
                JpowerAssert.createException(JpowerError.Business, IDCARD_NOT_LEGAL);
            }
        }

        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserDao.updateUserInfo(userVO);
    }

    @Override
    public boolean updatePassword(String oldPw, String newPw) {
        CoreUser user = coreUserDao.getById(ShieldUtil.getUserIdThrow());
        if (Fc.isNull(user) || !this.validatePassword(user.getLoginId(), oldPw, user.getTenantCode())) {
            JpowerAssert.createException(JpowerError.Business, "原"+PASSWORD_ERROR);
        }
        CacheUtil.clear(CacheNames.USER_KEY);
        return coreUserDao.updatePassword(DigestUtil.pwdEncrypt(newPw), Collections.singletonList(user.getId()));
    }

    @Override
    public Pg<UserVO> pageByRoleId(Map<String, Object> map) {
        return coreUserDao.pageByRoleId(map);
    }

	/**
	 * 据用户id查询用户信息
	 *
	 * @param id 用户id
	 * @return 用户信息
	 */
	@Override
	public LoginUserVO userInfo(Long id) {
		return coreUserDao.userInfo(id);
	}

}
