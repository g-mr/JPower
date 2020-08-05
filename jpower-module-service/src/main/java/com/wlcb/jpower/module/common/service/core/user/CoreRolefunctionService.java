package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.role.TbCoreRoleFunction;

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
    Integer addRolefunctions(String roleId, String functionIds);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过角色和菜单查询是否有权限
     * @Date 14:00 2020-07-31
     * @Param [toStrList, id]
     * @return com.wlcb.jpower.module.common.utils.StringUtil
     **/
    Integer countByRoleIdsAndFunctionId(List<String> toStrList, String id);
}
