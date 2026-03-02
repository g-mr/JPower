package top.jpower.system.service.role;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.role.CoreRoleFunction;
import top.jpower.system.vo.RoleFunctionSaveVO;
import top.jpower.system.vo.RoleFunctionVO;

import java.util.List;

/**
 * 角色功能服务接口
 * 
 * @author mr.g
 */
public interface CoreRoleFunctionService extends BaseService<CoreRoleFunction> {

    /**
     * 通过角色ID查询权限菜单
     * 
     * @author mr.g
     * @param roleId 角色ID
     * @return 角色功能映射列表
     */
    List<RoleFunctionVO> selectRoleFunctionByRoleId(Long roleId);

	/**
	 * 保存权限
	 *
	 * @author mr.g
	 * @param roleFunctionSaveVO 数据
	 * @return 保存结果
	 */
	boolean addRoleFunctions(RoleFunctionSaveVO roleFunctionSaveVO);
}
