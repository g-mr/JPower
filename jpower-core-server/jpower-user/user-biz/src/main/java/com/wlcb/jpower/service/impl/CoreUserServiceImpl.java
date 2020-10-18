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
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
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

    @Override
    public Boolean insertBatch(List<TbCoreUser> list) {
        return coreUserDao.saveBatch(list);
    }

    @Override
    public Boolean updateUsersRole(String userIds, String roleIds) {
        //先删除用户原有角色
        List<String> uIds = Fc.toStrList(userIds);

        LambdaQueryWrapper wrapper = new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,uIds);
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
    public TbCoreUser selectByPhone(String phone) {
        QueryWrapper wrapper = new QueryWrapper<TbCoreUserRole>();
        wrapper.eq("telephone",phone);
        return coreUserDao.getOne(wrapper);
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
        return coreUserDao.update(new TbCoreUser(),wrapper);
    }

}
