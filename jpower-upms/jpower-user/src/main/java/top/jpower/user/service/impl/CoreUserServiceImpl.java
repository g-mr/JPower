package top.jpower.user.service.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.NumberUtil;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import top.jpower.common.constants.DefaultValConstants;
import top.jpower.common.constants.ParamsConstants;
import top.jpower.common.enums.ActivationStatusEnum;
import top.jpower.common.enums.IdTypeEnum;
import top.jpower.common.enums.UserTypeEnum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.rsp.Pg;
import top.jpower.core.util.utils.DigestUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MD5;
import top.jpower.core.util.utils.UuidUtil;
import top.jpower.jpower.cache.SystemCache;
import top.jpower.user.api.cache.UserCache;
import top.jpower.jpower.cache.param.ParamConfig;
import top.jpower.user.dbs.dao.TbCoreUserDao;
import top.jpower.user.dbs.dao.TbCoreUserRoleDao;
import top.jpower.user.dbs.dao.mapper.TbCoreUserMapper;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dbs.entity.TbCoreUserRole;
import top.jpower.jpower.dbs.entity.tenant.TbCoreTenant;
import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.page.PaginationContext;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.user.dbs.entity.CoreUser;
import top.jpower.user.service.CoreUserService;
import top.jpower.jpower.vo.UserVo;
import top.jpower.user.vo.UserVO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static top.jpower.common.constants.CacheNames.TOKEN_USER_KEY;
import static top.jpower.core.dbs.tenant.TenantConstant.DEFAULT_TENANT_CODE;
import static top.jpower.core.dbs.tenant.TenantConstant.TENANT_ACCOUNT_NUMBER;
import static top.jpower.core.dbs.tenant.TenantConstant.getAccountNumber;

/**
 * @author mr.gmac
 */
@Slf4j
@AllArgsConstructor
@Service
public class CoreUserServiceImpl extends BaseServiceImpl<TbCoreUserMapper, CoreUser> implements CoreUserService {

    private TbCoreUserDao coreUserDao;
    private TbCoreUserRoleDao coreUserRoleDao;
    private RedisService redisService;

    @Override
    public Pg<UserVO> listPage(CoreUser coreUser) {
        Pg<UserVO> userVo = coreUserDao.listVo(coreUser);
        //查询用户在线信息
        userVo.getList().forEach(user-> user.setOnLine(redisService.keys(TOKEN_USER_KEY+user.getId() + StringPool.COLON + StringPool.ASTERISK).size()));
        return userVo;
    }

    @Override
    public List<UserVo> list(TbCoreUser coreUser) {
        return coreUserDao.listVo(coreUser);
    }

    @Override
    public boolean save(TbCoreUser coreUser) {
        setActivationStatus(coreUser);
        return coreUserDao.save(coreUser);
    }

    private void setActivationStatus(TbCoreUser coreUser) {
        if (Fc.isNull(coreUser.getActivationStatus())){
            Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION, DefaultValConstants.DEFAULT_USER_ACTIVATION);
            coreUser.setActivationStatus(isActivation);
        }

        if (!ActivationStatusEnum.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UuidUtil.create10UUidNum());
            coreUser.setActivationStatus(ActivationStatusEnum.ACTIVATION_NO.getValue());
        }
    }


    @Override
    public Boolean delete(List<Long> ids) {
        ids = new ArrayList<>(ids);

        if(Fc.contains(ids, RoleConstant.ROOT_ID) || Fc.contains(ids, RoleConstant.ANONYMOUS_ID)){
            ids.removeIf(obj -> NumberUtil.equals(obj,RoleConstant.ROOT_ID) || NumberUtil.equals(obj,RoleConstant.ANONYMOUS_ID));

            if (ids.size() <= 0){
                throw new BusinessException("超级用户和匿名用户不可删除");
            }
        }

        boolean is = coreUserDao.removeByIds(ids);
        if (is){
            coreUserRoleDao.removeReal(new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,ids));
        }
        return is;
    }

    @Override
    public Boolean update(TbCoreUser coreUser) {

        TbCoreUser user = coreUserDao.getById(coreUser.getId());
        JpowerAssert.notNull(user, JpowerError.NotFind,"该用户");

        //如果修改了角色或者修改了租户则需要去更新角色
        if (Fc.isNotBlank(coreUser.getRoleIds()) ||
                (Fc.isNotBlank(coreUser.getTenantCode()) && !Fc.equalsValue(user.getTenantCode(),coreUser.getTenantCode()))){
            //如果修改了租户则需要把原来的角色全部去掉
            if (Fc.isNotBlank(coreUser.getTenantCode()) && !Fc.equalsValue(user.getTenantCode(),coreUser.getTenantCode())){
                coreUser.setRoleIds(null);
            }
            updateUsersRole(Collections.singletonList(coreUser.getId()),Fc.toLongList(coreUser.getRoleIds()));
        }

        return coreUserDao.updateById(coreUser);
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId,String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getLoginId,loginId);
        if (ShieldUtil.isRoot()){
            tenantCode = Fc.isBlank(tenantCode) ? DEFAULT_TENANT_CODE : tenantCode;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public boolean validatePassword(String account, String password, String tenantCode) {
        String userPassword = coreUserDao.getPassword(account, tenantCode);
        return DigestUtil.checkPwd(password, userPassword);
    }

    @Override
    public UserVo selectUserById(Long id) {
        return coreUserDao.conver(getBaseMapper().selectAllById(id));
    }

    @Override
    public Page<UserVo> page(Page<TbCoreUser> page, Wrapper<TbCoreUser> queryWrapper) {
        return coreUserDao.pageConver(super.page(page,queryWrapper));
    }

    @Override
    public TbCoreUser selectUserByOtherCode(String otherCode, String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper()
                .lambda().eq(TbCoreUser::getOtherCode,otherCode);
        if (ShieldUtil.isRoot()){
            queryWrapper.eq(TbCoreUser::getTenantCode,Fc.isBlank(tenantCode)? DEFAULT_TENANT_CODE :tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public boolean saveUser(TbCoreUser user) {
        if (coreUserDao.save(user)){
            List<TbCoreUserRole> userRoleList = new ArrayList<>();
            Fc.toLongList(user.getRoleIds()).forEach(roleId->{
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setUserId(user.getId());
                userRole.setRoleId(roleId);
                userRoleList.add(userRole);
            });

            if (Fc.isNotEmpty(userRoleList)){
                return coreUserRoleDao.saveBatch(userRoleList);
            }
        }
        return false;
    }

    @Override
    public Boolean updateUserPassword(List<Long> ids, String pass) {
        return coreUserDao.update(new UpdateWrapper<TbCoreUser>().lambda().set(TbCoreUser::getPassword,pass).in(TbCoreUser::getId,ids));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 去除重复以及正确验证
     * @date 1:46 2020/10/20 0020
     */
    private <T> Predicate<T> filterUser(Function<? super T, TbCoreUser> keyExtractor) {
        Map<Object, Boolean> loginIdMap = new ConcurrentHashMap();
        Map<Object, Boolean> phoneMap = new ConcurrentHashMap();

        return object ->
            Fc.isNull(loginIdMap.putIfAbsent(keyExtractor.apply(object).getLoginId(), Boolean.TRUE))&&
                    Fc.isNotBlank(keyExtractor.apply(object).getTelephone()) ? Fc.isNull(phoneMap.putIfAbsent(keyExtractor.apply(object).getTelephone(), Boolean.TRUE)) : Boolean.TRUE;
    }

    @Override
    public boolean insertBatch(List<TbCoreUser> list,boolean isCover) {

        List<TbCoreUser> userList = new ArrayList<>();

        String password = DigestUtil.pwdEncrypt(MD5.md5HexToUpperCase(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD, DefaultValConstants.DEFAULT_USER_PASSWORD)));

        for (TbCoreUser coreUser : list) {
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
                coreUser.setTenantCode(Fc.toStr(ShieldUtil.getTenantCode(),DEFAULT_TENANT_CODE));
            }

            setActivationStatus(coreUser);

            TbCoreUser user = UserCache.getUserByLoginId(coreUser.getLoginId(),coreUser.getTenantCode());
            if (Fc.notNull(user)){
                if (isCover){
                    coreUser.setId(user.getId());
                }else {
                    //如果loginID重复但是不进行覆盖则去除
                    continue;
                }
            }

            if (Fc.isNotBlank(coreUser.getTelephone())){
                user = UserCache.getUserByPhone(coreUser.getTelephone(),coreUser.getTenantCode());
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

        List<String> tenantCodes = userList.stream().map(TbCoreUser::getTenantCode).distinct().collect(Collectors.toList());
        tenantCodes.forEach(tenantCode -> {

            TbCoreTenant tenant = SystemCache.getTenantByCode(tenantCode);
            if (Fc.isNull(tenant)){
                throw new BusinessException(tenantCode+"租户不存在");
            }
            long accountNumber = getAccountNumber(tenant.getLicenseKey());
            if (!Fc.equals(accountNumber, TENANT_ACCOUNT_NUMBER)){
                long count = coreUserDao.count(Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTenantCode,tenantCode));
                if (!NumberUtil.equals(accountNumber,-1L) && count >= accountNumber){
                    throw new BusinessException(tenant.getTenantName()+"租户账号额度不足");
                }
            }

        });

        return coreUserDao.saveOrUpdateBatch(userList);
    }

    @Override
    public Boolean updateUsersRole(List<Long> userIds, List<Long> roleIds) {

        LambdaQueryWrapper<TbCoreUserRole> wrapper = new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,userIds);
        coreUserRoleDao.removeReal(wrapper);

        if (Fc.isNotEmpty(roleIds)){

            List<TbCoreUserRole> userRoles = new ArrayList<>();
            for (Long rId : roleIds) {
                for (Long userId : userIds) {
                    TbCoreUserRole userRole = new TbCoreUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(rId);
                    userRoles.add(userRole);
                }
            }

            //如果修改超级用户，并且角色不包含超级用户角色，则给超级用户添加超级用户角色
            if (Fc.contains(userIds,RoleConstant.ROOT_ID) && !Fc.contains(roleIds,RoleConstant.ROOT_ID)){
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setUserId(RoleConstant.ROOT_ID);
                userRole.setRoleId(RoleConstant.ROOT_ID);
                userRoles.add(userRole);
            }
            //如果修改匿名用户，并且角色不包含匿名用户角色，则给匿名用户添加匿名用户角色
            if (Fc.contains(userIds,RoleConstant.ANONYMOUS_ID) && !Fc.contains(roleIds,RoleConstant.ANONYMOUS_ID)){
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setUserId(RoleConstant.ANONYMOUS_ID);
                userRole.setRoleId(RoleConstant.ANONYMOUS_ID);
                userRoles.add(userRole);
            }

            if (userRoles.size() > 0){
                return coreUserRoleDao.saveBatch(userRoles);
            }
        }
        return true;
    }

    @Override
    public boolean addRoleUsers(Long roleId, List<Long> userIds) {
        List<TbCoreUserRole> list = new ArrayList<>();
        userIds.forEach((userId)->{
            TbCoreUserRole userRole = new TbCoreUserRole();
            userRole.setRoleId(roleId);
            userRole.setUserId(userId);
            list.add(userRole);
        });

        return coreUserRoleDao.saveBatch(list);
    }

    @Override
    public boolean deleteRoleUsers(Long roleId, List<Long> userIds) {

        userIds.removeIf(userId->
                (Fc.equalsValue(roleId,RoleConstant.ROOT_ID)&&Fc.equalsValue(userId,RoleConstant.ROOT_ID))
                ||
                (Fc.equalsValue(roleId,RoleConstant.ANONYMOUS_ID)&&Fc.equalsValue(userId,RoleConstant.ANONYMOUS_ID)));

        if (userIds.size() <= 0){
            throw new BusinessException("不可去除超级用户或匿名用户的角色");
        }

        return coreUserRoleDao.removeReal(Condition.<TbCoreUserRole>getQueryWrapper()
                .lambda()
                .eq(TbCoreUserRole::getRoleId,roleId)
                .in(TbCoreUserRole::getUserId,userIds));
    }

    @Override
    public TbCoreUser selectByPhone(String phone,String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTelephone,phone);
        if (ShieldUtil.isRoot()){
            tenantCode = Fc.isBlank(tenantCode)? DEFAULT_TENANT_CODE:tenantCode;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public Boolean updateLoginInfo(Long id) {
        return coreUserDao.update(Wrappers.<TbCoreUser>lambdaUpdate()
                .setSql("login_count = ifnull(login_count,0)+1")
                .set(TbCoreUser::getLastLoginTime, new Date())
                .eq(TbCoreUser::getId,id));
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
        return coreUserDao.updatePhone(userId, phone);
    }

    @Override
    public boolean updateEmail(String email, Long userId) {
        return coreUserDao.updateEmail(userId, email);
    }

}
