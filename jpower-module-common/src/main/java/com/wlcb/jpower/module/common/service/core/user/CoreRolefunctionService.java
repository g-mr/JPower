package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;

public interface CoreRolefunctionService {

    /**
     * @author 郭丁志
     * @Description //TODO 通过角色ID查询权限菜单
     * @date 23:58 2020/5/26 0026
     * @param roleId 角色ID
     * @return com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction
     */
    TbCoreRoleFunction selectRoleFunctionByRoleId(String roleId);

    /**
     * @author 郭丁志
     * @Description //TODO 新增角色权限
     * @date 0:21 2020/5/27 0027
     * @param roleId
     * @param functionIds
     * @return java.lang.Integer
     */
    Integer addRolefunctions(String roleId, String functionIds);
}
