package com.wlcb.jpower.module.common.service.core.user.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.UUIDUtil;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserMapper;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author mr.gmac
 */
@Service("coreUserService")
public class CoreUserServiceImpl implements CoreUserService {

    @Autowired
    private TbCoreUserMapper coreUserMapper;
    @Autowired
    private TbCoreUserRoleMapper coreUserRoleMapper;

    @Override
    public List<TbCoreUser> list(TbCoreUser coreUser) {

        EntityWrapper wrapper = new EntityWrapper<TbCoreUser>();

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

        if (coreUser.getStatus() != null){
            wrapper.eq("status",coreUser.getStatus());
        }

        wrapper.orderBy("create_time",false);

        return coreUserMapper.selectList(wrapper);
    }

    @Override
    public Integer add(TbCoreUser coreUser) {
        if (coreUser.getActivationStatus() == null){
            Integer isActivation = ParamConfig.getInt("isActivation");

            coreUser.setActivationStatus(isActivation);
        }

        if (!ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_YES.getValue().equals(coreUser.getActivationStatus())){
            coreUser.setActivationCode(UUIDUtil.create10UUidNum());
            coreUser.setActivationStatus(ConstantsEnum.ACTIVATION_STATUS.ACTIVATION_NO.getValue());
        }

        coreUser.setUpdateUser(coreUser.getCreateUser());
        return coreUserMapper.insert(coreUser);
    }

    @Override
    public Integer delete(String ids) {
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        return coreUserMapper.deleteBatchIds(list);
    }

    @Override
    public Integer update(TbCoreUser coreUser) {
        return coreUserMapper.updateById(coreUser);
    }

    @Override
    public TbCoreUser selectUserLoginId(String loginId) {
        TbCoreUser coreUser = new TbCoreUser();
        coreUser.setLoginId(loginId);
        return coreUserMapper.selectOne(coreUser);
    }

    @Override
    public TbCoreUser selectUserById(String id) {
        return coreUserMapper.selectById(id);
    }

    @Override
    public Integer updateUserPassword(String ids, String pass) {
        EntityWrapper wrapper = new EntityWrapper<TbCoreUser>();
        wrapper.in("id",ids);
        return coreUserMapper.updateForSet("password = "+pass,wrapper);
    }

    @Override
    public Integer insterBatch(List<TbCoreUser> list) {
        return coreUserMapper.insertList(list);
    }

    @Override
    public Integer updateUserRole(String userId, String roleIds) {
        String[] rIds = roleIds.split(",");
        List<TbCoreUserRole> userRoles = new ArrayList<>();
        for (String rId : rIds) {
            TbCoreUserRole userRole = new TbCoreUserRole();
            userRole.setId(UUIDUtil.getUUID());
            userRole.setUserId(userId);
            userRole.setRoleId(rId);
            userRoles.add(userRole);
        }

        //先删除用户原有角色
        Map<String, Object> columnMap = new HashMap<String, Object>();
        columnMap.put("user_id", userId);
        coreUserRoleMapper.deleteByMap(columnMap);

        if (userRoles.size() > 0){
            Integer count = coreUserRoleMapper.insertList(userRoles);
            return count;
        }
        return 1;
    }
}
