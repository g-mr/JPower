package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.wlcb.jpower.module.common.service.base.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.constants.ParamsConstants;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserRoleDao;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreUserMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author mr.gmac
 */
@Slf4j
@Service("coreUserService")
public class CoreUserServiceImpl extends BaseServiceImpl<TbCoreUserMapper,TbCoreUser> implements CoreUserService {

    @Autowired
    private TbCoreUserMapper coreUserMapper;
    @Autowired
    private TbCoreUserDao coreUserDao;
    @Autowired
    private TbCoreUserRoleDao coreUserRoleDao;

    @Override
    public List<TbCoreUser> list(TbCoreUser coreUser) {

        QueryWrapper wrapper = new QueryWrapper<TbCoreUser>();

        if (StringUtils.isNotBlank(coreUser.getOrgId())){
            wrapper.eq("org_id",coreUser.getOrgId());
        }

        if (StringUtils.isNotBlank(coreUser.getLoginId())){
            wrapper.eq("login_id",coreUser.getLoginId());
        }

        if (StringUtils.isNotBlank(coreUser.getUserName())){
            wrapper.eq("user_name",coreUser.getUserName());
        }

        if (StringUtils.isNotBlank(coreUser.getIdNo())){
            wrapper.like("id_no",coreUser.getIdNo());
        }

        if (coreUser.getUserType() != null){
            wrapper.eq("user_type",coreUser.getUserType());
        }

        if (StringUtils.isNotBlank(coreUser.getTelephone())){
            wrapper.like("telephone",coreUser.getTelephone());
        }

        if (coreUser.getActivationStatus() != null){
            wrapper.eq("activation_status",coreUser.getActivationStatus());
        }

        wrapper.orderByDesc("create_time");

        return coreUserDao.list(wrapper);
    }

    @Override
    public Boolean add(TbCoreUser coreUser) {
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
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        boolean is = coreUserDao.removeByIds(list);
        if (is){
            coreUserRoleDao.remove(new QueryWrapper<TbCoreUserRole>().lambda().in(TbCoreUserRole::getUserId,list));
        }
        return is;
    }

    @Override
    public Boolean update(TbCoreUser coreUser) {
        return coreUserDao.updateById(coreUser);
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId) {
        return coreUserDao.getOne(new QueryWrapper<TbCoreUser>().lambda().eq(TbCoreUser::getLoginId,loginId).eq(TbCoreUser::getStatus,1));
    }

    @Override
    public TbCoreUser selectUserById(String id) {
        return coreUserMapper.selectAllById(id);
    }

    @Override
    public Boolean updateUserPassword(String ids, String pass) {
        UpdateWrapper wrapper = new UpdateWrapper<TbCoreUser>();
        wrapper.in("id",ids);
        wrapper.set("password",pass);
        return coreUserDao.update(new TbCoreUser(),wrapper);
    }

    @Override
    public Boolean insterBatch(List<TbCoreUser> list) {
        return coreUserDao.saveBatch(list);
    }

    @Override
    public Boolean updateUsersRole(String userIds, String roleIds) {
        String[] rIds = roleIds.split(",");
        String[] uIds = userIds.split(",");
        List<TbCoreUserRole> userRoles = new ArrayList<>();
        for (String rId : rIds) {
            for (String userId : uIds) {
                TbCoreUserRole userRole = new TbCoreUserRole();
                userRole.setId(UUIDUtil.getUUID());
                userRole.setUserId(userId);
                userRole.setRoleId(rId);
                userRoles.add(userRole);
            }
        }

        //先删除用户原有角色
        QueryWrapper wrapper = new QueryWrapper<TbCoreUserRole>();
        wrapper.in("user_id",uIds);
        coreUserRoleDao.remove(wrapper);

        if (userRoles.size() > 0){
            Boolean is = coreUserRoleDao.saveBatch(userRoles);
            return is;
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
