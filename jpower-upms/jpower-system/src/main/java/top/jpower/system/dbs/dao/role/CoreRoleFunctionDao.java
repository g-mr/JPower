package top.jpower.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleFunctionMapper;
import top.jpower.system.dbs.entity.function.CoreFunction;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;
import top.jpower.system.vo.RoleFunctionVO;

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
}
