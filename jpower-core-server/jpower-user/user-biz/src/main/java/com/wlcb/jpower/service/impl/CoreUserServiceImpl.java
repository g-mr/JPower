package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.github.pagehelper.PageInfo;
import com.wlcb.jpower.config.param.ParamConfig;
import com.wlcb.jpower.config.system.SystemCache;
import com.wlcb.jpower.dbs.dao.TbCoreUserDao;
import com.wlcb.jpower.dbs.dao.TbCoreUserRoleDao;
import com.wlcb.jpower.dbs.dao.mapper.TbCoreUserMapper;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.dbs.entity.TbCoreUserRole;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.tenant.TenantConstant;
import com.wlcb.jpower.service.CoreUserService;
import com.wlcb.jpower.vo.UserVo;
import com.wlcb.jpower.wrapper.UserWrapper;
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
        List<String> listOrgId = Fc.isNotBlank(coreUser.getOrgId())?SystemCache.getChildIdOrgById(coreUser.getOrgId()):null;

        PaginationContext.startPage();
        List<TbCoreUser> list = coreUserDao.getBaseMapper().selectUserList(coreUser,listOrgId);
        return new PageInfo<>(UserWrapper.builder().build().listVO(list));
    }

    @Override
    public List<UserVo> list(TbCoreUser coreUser) {
        List<String> listOrgId = Fc.isNotBlank(coreUser.getOrgId())?SystemCache.getChildIdOrgById(coreUser.getOrgId()):null;

        List<TbCoreUser> list = coreUserDao.getBaseMapper().selectUserList(coreUser,listOrgId);
        return UserWrapper.builder().build().listVO(list);
    }


    @Override
    public boolean save(TbCoreUser coreUser) {
        if (Fc.isNull(coreUser.getActivationStatus())){
            Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION, ConstantsUtils.DEFAULT_USER_ACTIVATION);
            coreUser.setActivationStatus(isActivation);
        }

        if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UUIDUtil.create10UUidNum());
            coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
        }

        return coreUserDao.save(coreUser);
    }

    @Override
    public Boolean delete(String ids) {
        List<String> list = Fc.toStrList(ids);
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
        if (SecureUtil.isRoot() && Fc.isBlank(tenantCode)){
            tenantCode = TenantConstant.DEFAULT_TENANT_CODE;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public UserVo selectUserById(String id) {
        return UserWrapper.builder().build().entityVO(baseMapper.selectAllById(id));
    }

    @Override
    public Boolean updateUserPassword(String ids, String pass) {
        return coreUserDao.update(new UpdateWrapper<TbCoreUser>().lambda().set(TbCoreUser::getPassword,pass).in(TbCoreUser::getId,Fc.toStrList(ids)));
    }

    /**
     * @author 郭丁志
     * @Description //TODO 去除重复以及正确验证，这段写的真恶心
     * @date 1:46 2020/10/20 0020
     */
    private <T> Predicate<T> filterUser(Function<? super T, TbCoreUser> keyExtractor) {
        Map<Object, Boolean> loginIdMap = new ConcurrentHashMap();
        Map<Object, Boolean> phoneMap = new ConcurrentHashMap();

        return (object) ->{

            String loginId = keyExtractor.apply(object).getLoginId();
            String phone = keyExtractor.apply(object).getTelephone();
            String email = keyExtractor.apply(object).getEmail();
            Integer idType = keyExtractor.apply(object).getIdType();
            String idNo = keyExtractor.apply(object).getIdNo();

            if (Fc.isBlank(loginId)){
                return Boolean.FALSE;
            }
            if (Fc.isNotBlank(phone) && !StrUtil.isPhone(phone)){
                return Boolean.FALSE;
            }
            if (Fc.isNotBlank(email) && !StrUtil.isEmail(email)){
                return Boolean.FALSE;
            }
            if (Fc.isNotBlank(idNo) && ConstantsEnum.ID_TYPE.ID_CARD.getValue().equals(idType) && !StrUtil.cardCodeVerifySimple(idNo)){
                return Boolean.FALSE;
            }

            return Fc.isNull(loginIdMap.putIfAbsent(loginId, Boolean.TRUE))&&
                    Fc.isNotBlank(phone) ? Fc.isNull(phoneMap.putIfAbsent(phone, Boolean.TRUE)) : Boolean.TRUE;
        };
    }

    /**
     * @author 郭丁志
     * @Description //TODO 过滤数据库已经存在的数据
     * @date 1:54 2020/10/20 0020
     */
    private boolean isDataBaseUser(List<TbCoreUser> databaseUserList, TbCoreUser coreUser) {
        for (TbCoreUser user : databaseUserList) {
            if ((Fc.isNotBlank(coreUser.getTelephone()) && Fc.equals(user.getTelephone(),coreUser.getTelephone())) || Fc.equals(user.getLoginId(),coreUser.getLoginId())){
                return true;
            }
        }
        return false;
    }

    @Override
    public Integer insertBatch(List<TbCoreUser> list) {
        list = list.stream().filter(filterUser(o -> o)).collect(Collectors.toList());

        List<TbCoreUser> userList = new ArrayList<>();

        List<TbCoreUser> databaseUserList = queryRepeatUser(list);

        String password = DigestUtil.encrypt(MD5.parseStrToMd5U32(ParamConfig.getString(ParamsConstants.USER_DEFAULT_PASSWORD,ConstantsUtils.DEFAULT_USER_PASSWORD)));
        Integer isActivation = ParamConfig.getInt(ParamsConstants.IS_ACTIVATION,ConstantsUtils.DEFAULT_USER_ACTIVATION);
        for (TbCoreUser coreUser : list) {
            if (!isDataBaseUser(databaseUserList,coreUser)){
                coreUser.setPassword(password);
                coreUser.setUserType(ConstantsEnum.USER_TYPE.USER_TYPE_SYSTEM.getValue());

                //判断用户是否指定激活，如果没有指定就去读取默认配置
                if (coreUser.getActivationStatus() == null){
                    coreUser.setActivationStatus(isActivation);
                }
                // 如果不是激活状态则去生成激活码
                if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
                    coreUser.setActivationCode(UUIDUtil.create10UUidNum());
                    coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
                }
                userList.add(coreUser);
            }
        }

        return coreUserDao.saveBatch(userList)?userList.size():0;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 查询重复用户
     * @param list 已有用户
     */
    private List<TbCoreUser> queryRepeatUser(List<TbCoreUser> list) {
        if (list.size() <= 0){
            return new ArrayList<>();
        }

        List<String> listLoginId = list.stream().map(TbCoreUser::getLoginId).distinct().collect(Collectors.toList());
        List<String> listPhone = list.stream().map(TbCoreUser::getTelephone).distinct().collect(Collectors.toList());

        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper().lambda()
                .and(wrapper -> wrapper.in(TbCoreUser::getLoginId, listLoginId).or().in(TbCoreUser::getTelephone, listPhone));

        //导入只可导入自己租户用户
        if (SecureUtil.isRoot()){
            queryWrapper.eq(TbCoreUser::getTenantCode,TenantConstant.DEFAULT_TENANT_CODE);
        }
        return coreUserDao.list(queryWrapper);
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

            if (userRoles.size() > 0){
                Boolean is = coreUserRoleDao.saveBatch(userRoles);
                return is;
            }
        }
        return true;
    }

    @Override
    public TbCoreUser selectByPhone(String phone,String tenantCode) {
        LambdaQueryWrapper<TbCoreUser> queryWrapper = Condition.<TbCoreUser>getQueryWrapper().lambda().eq(TbCoreUser::getTelephone,phone);
        if (SecureUtil.isRoot() && Fc.isBlank(tenantCode)){
            tenantCode = TenantConstant.DEFAULT_TENANT_CODE;
            queryWrapper.eq(TbCoreUser::getTenantCode,tenantCode);
        }
        return coreUserDao.getOne(queryWrapper);
    }

    @Override
    public TbCoreUser selectByUserNameAndId(String id, String username) {
        LambdaQueryWrapper<TbCoreUser> wrapper = new QueryWrapper<TbCoreUser>().lambda();
        wrapper.eq(TbCoreUser::getLoginId,username);
        wrapper.eq(TbCoreUser::getId,id);
        return coreUserDao.getOne(wrapper);
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
