package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.cache.param.ParamConfig;
import com.wlcb.jpower.dbs.dao.TbCoreUserDao;
import com.wlcb.jpower.dbs.dao.TbCoreUserRoleDao;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import com.wlcb.jpower.dbs.entity.tenant.TbCoreTenant;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.CoreUserService;
import com.wlcb.jpower.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.wlcb.jpower.module.tenant.TenantConstant.*;

/**
 * @author mr.gmac
 */
@Slf4j
@AllArgsConstructor
@Service("coreUserService")
public class CoreUserServiceImpl extends BaseServiceImpl<TbCoreUserMapper, TbCoreUser> implements CoreUserService {

    private TbCoreUserDao coreUserDao;
    private TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public PageInfo<UserVo> listPage(TbCoreUser coreUser) {
        PaginationContext.startPage();
        return new PageInfo<>(coreUserDao.listVo(coreUser));
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
            Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION, ConstantsUtils.DEFAULT_USER_ACTIVATION);
            coreUser.setActivationStatus(isActivation);
        }

        if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UUIDUtil.create10UUidNum());
            coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
        }
    }


    @Override
    public Boolean delete(String ids) {
        List<String> list = new ArrayList<>(Fc.toStrList(ids));

        if(Fc.contains(list, RoleConstant.ROOT_ID) || Fc.contains(list, RoleConstant.ANONYMOUS_ID)){
            list.removeIf(obj -> StringUtil.equals(obj,RoleConstant.ROOT_ID) || StringUtil.equals(obj,RoleConstant.ANONYMOUS_ID));

            if (list.size() <= 0){
                throw new BusinessException("超级用户和匿名用户不可删除");
            }
        }

        boolean is = coreUserDao.removeByIds(list);
        if (is){
            coreUserRoleDao.removeReal(new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,list));
        }
        return is;
    }

    @Override
    public Boolean update(TbCoreUser coreUser) {
        boolean is = coreUserDao.updateById(coreUser);
        //如果成功并且存在角色则去修改角色
        if (is && Fc.isNotBlank(coreUser.getRoleIds())){
            updateUsersRole(coreUser.getId(),coreUser.getRoleIds());
        }
        return is;
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId,String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getLoginId,loginId);
        if (SecureUtil.isRoot()){
            tenantCode = Fc.isBlank(tenantCode)?DEFAULT_TENANT_CODE:tenantCode;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public UserVo selectUserById(String id) {
        return coreUserDao.conver(getBaseMapper().selectAllById(id));
    }

    @Override
    public UserVo getById(String id) {
        return coreUserDao.conver(super.getById(id));
    }

    @Override
    public Page<UserVo> page(Page<TbCoreUser> page, Wrapper<TbCoreUser> queryWrapper) {
        return coreUserDao.pageConver(super.page(page,queryWrapper));
    }

    @Override
    public TbCoreUser selectUserByOtherCode(String otherCode, String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper()
                .lambda().eq(TbCoreUser::getOtherCode,otherCode);
        if (SecureUtil.isRoot()){
            queryWrapper.eq(TbCoreUser::getTenantCode,Fc.isBlank(tenantCode)? DEFAULT_TENANT_CODE :tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public boolean saveAdmin(TbCoreUser user, String roleId) {
        if (coreUserDao.save(user)){
            TbCoreUserRole userRole = new TbCoreUserRole();
            userRole.setUserId(user.getId());
            userRole.setRoleId(roleId);
            return coreUserRoleDao.save(userRole);
        }
        return false;
    }

    @Override
    public Boolean updateUserPassword(String ids, String pass) {
        return coreUserDao.update(new UpdateWrapper<TbCoreUser>().lambda().set(TbCoreUser::getPassword,pass).in(TbCoreUser::getId,Fc.toStrList(ids)));
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

        String password = DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD,ConstantsUtils.DEFAULT_USER_PASSWORD)));

        for (TbCoreUser coreUser : list) {
            if (Fc.isBlank(coreUser.getLoginId())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getTelephone()) && !StrUtil.isPhone(coreUser.getTelephone())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getEmail()) && !StrUtil.isEmail(coreUser.getEmail())){
                continue;
            }
            if (Fc.isNotBlank(coreUser.getIdNo()) && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(coreUser.getIdType()) && !StrUtil.cardCodeVerifySimple(coreUser.getIdNo())){
                continue;
            }

            coreUser.setPassword(password);
            coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());

            if (SecureUtil.isRoot()){
                coreUser.setTenantCode(Fc.isBlank(coreUser.getTenantCode())?SecureUtil.getTenantCode():coreUser.getTenantCode());
            }else {
                coreUser.setTenantCode(SecureUtil.getTenantCode());
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
                        if (Fc.isNotBlank(coreUser.getId()) && !Fc.equals(coreUser.getId(),user.getId())){
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
                if (count >= accountNumber){
                    throw new BusinessException(tenant.getTenantName()+"租户账号额度不足");
                }
            }

        });

        return coreUserDao.saveOrUpdateBatch(userList);
    }

    @Override
    public Boolean updateUsersRole(String userIds, String roleIds) {
        //先删除用户原有角色
        List<String> uIds = Fc.toStrList(userIds);

        LambdaQueryWrapper<TbCoreUserRole> wrapper = new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,uIds);
        coreUserRoleDao.removeReal(wrapper);

        if (Fc.isNotBlank(roleIds)){
            List<String> rIds = Fc.toStrList(roleIds);

            List<TbCoreUserRole> userRoles = new ArrayList<>();
            for (String rId : rIds) {
                for (String userId : uIds) {
                    TbCoreUserRole userRole = new TbCoreUserRole();
                    userRole.setUserId(userId);
                    userRole.setRoleId(rId);
                    userRoles.add(userRole);
                }
            }

            //如果修改超级用户，并且角色不包含超级用户角色，则给超级用户添加超级用户角色
            if (Fc.contains(uIds,RoleConstant.ROOT_ID) && !Fc.contains(rIds,RoleConstant.ROOT_ID)){
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setUserId(RoleConstant.ROOT_ID);
                userRole.setRoleId(RoleConstant.ROOT_ID);
                userRoles.add(userRole);
            }
            //如果修改匿名用户，并且角色不包含匿名用户角色，则给匿名用户添加匿名用户角色
            if (Fc.contains(uIds,RoleConstant.ANONYMOUS_ID) && !Fc.contains(rIds,RoleConstant.ANONYMOUS_ID)){
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setUserId(RoleConstant.ANONYMOUS_ID);
                userRole.setRoleId(RoleConstant.ANONYMOUS_ID);
                userRoles.add(userRole);
            }

            if (userRoles.size() > 0){
                Boolean is = coreUserRoleDao.saveBatch(userRoles);
                return is;
            }
        }
        return true;
    }

    @Override
    public boolean addRoleUsers(String roleId, List<String> userIds) {
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
    public boolean deleteRoleUsers(String roleId, List<String> userIds) {

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
        if (SecureUtil.isRoot()){
            tenantCode = Fc.isBlank(tenantCode)? DEFAULT_TENANT_CODE:tenantCode;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public Boolean updateLoginInfo(TbCoreUser user) {
        LambdaUpdateWrapper<TbCoreUser> wrapper = new UpdateWrapper<TbCoreUser>().lambda()
        .set(TbCoreUser::getLoginCount,user.getLoginCount())
        .set(TbCoreUser::getLastLoginTime,user.getLastLoginTime())
        .eq(TbCoreUser::getId,user.getId());
        return coreUserDao.update(wrapper);
    }

}
