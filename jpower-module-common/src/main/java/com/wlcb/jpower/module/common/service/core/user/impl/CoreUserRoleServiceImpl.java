package com.wlcb.jpower.module.common.service.core.user.impl;

import com.wlcb.jpower.module.common.service.core.user.CoreUserRoleService;
import com.wlcb.jpower.module.dbs.dao.core.user.mapper.TbCoreUserRoleMapper;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreUserRole;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 郭丁志
 * @Description //TODO 用户角色
 * @date 22:45 2020/5/26 0026
 */
@Service("coreUserRoleService")
public class CoreUserRoleServiceImpl implements CoreUserRoleService {

    public TbCoreUserRoleMapper coreUserRoleMapper;

    @Override
    public List<TbCoreUserRole> selectUserRoleByUserId(String userId) {
        return coreUserRoleMapper.selectUserRoleByUserId(userId);
    }
}
