package com.wlcb.jpower.service.role;

import com.wlcb.jpower.dbs.entity.role.TbCoreRoleFunction;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreRolefunctionService extends BaseService<TbCoreRoleFunction> {

    /**
     * @author 郭丁志
     * @Description //TODO 通过角色ID查询权限菜单
     * @date 23:58 2020/5/26 0026
     * @param roleId 角色ID
     * @return com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction
     */
    List<Map<String,Object>> selectRoleFunctionByRoleId(String roleId);

    /**
     * @author 郭丁志
     * @Description //TODO 新增角色权限
     * @date 0:21 2020/5/27 0027
     * @param roleId
     * @param functionIds
     * @return java.lang.Integer
     */
    boolean addRolefunctions(String roleId, String functionIds);

}
