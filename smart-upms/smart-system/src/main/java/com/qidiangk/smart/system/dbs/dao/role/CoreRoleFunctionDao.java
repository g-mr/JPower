package com.qidiangk.smart.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import com.qidiangk.smart.system.dbs.dao.role.mapper.CoreRoleFunctionMapper;
import com.qidiangk.smart.system.dbs.entity.function.CoreFunction;
import com.qidiangk.smart.system.dbs.entity.role.CoreRoleFunction;
import com.qidiangk.smart.system.vo.RoleFunctionVO;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色功能数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleFunctionDao extends JpowerServiceImpl<CoreRoleFunctionMapper, CoreRoleFunction> {

    /**
     * 保存权限
     *
     * @author mr.g
     * @param functionIds 功能ID
     * @param roleId 角色ID
     * @return 是否存在
     **/
    public Boolean saveFunctions(List<Long> functionIds, Long roleId) {
        List<CoreRoleFunction> roleFunctions = new ArrayList<>();
        functionIds.forEach(functionId -> {
			CoreRoleFunction roleFunction = new CoreRoleFunction();
            roleFunction.setFunctionId(functionId);
            roleFunction.setRoleId(roleId);
            roleFunctions.add(roleFunction);
        });
        return super.saveBatch(roleFunctions);
    }

	/**
	 * 根据角色ID查询权限
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 * @return 角色权限列表
	 **/
	public List<RoleFunctionVO> selectRoleFunctionByRoleId(Long roleId) {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CoreRoleFunction::getRoleId, CoreRoleFunction::getFunctionId, CoreRoleFunction::getFunctionName)
				.select(CoreFunction::getUrl)
				.leftJoin(CoreFunction.class).on(CoreRoleFunction::getFunctionId, CoreFunction::getId)
				.eq(CoreRoleFunction::getRoleId, roleId), RoleFunctionVO.class);
	}

	/**
	 * 根据角色ID删除权限
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 **/
	public void removeRealByRoleId(Long roleId) {
		super.removeReal(Wrappers.getQueryWrapper().eq(CoreRoleFunction::getRoleId, roleId));
	}

	/**
	 * 根据功能ID删除权限
	 *
	 * @author mr.g
	 * @param ids 功能ID
	 **/
	public void removeRealByFunctionId(List<Long> ids) {
		super.removeReal(Wrappers.getQueryWrapper().in(CoreRoleFunction::getFunctionId, ids));
	}

	public List<Long> listFunctionIdByRole(List<Long> roleIds) {
		return super.objListAs(Wrappers.getQueryWrapper()
					.select(CoreRoleFunction::getFunctionId)
					.in(CoreRoleFunction::getRoleId, roleIds), Long.class);
	}
}
