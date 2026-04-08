package top.jpower.system.service.role;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.role.CoreRole;

import java.util.List;
import java.util.Map;

/**
 * 角色服务接口
 *
 * @author mr.g
 */
public interface CoreRoleService extends BaseService<CoreRole> {

	/**
	 * 新增角色
	 *
	 * @author mr.g
	 * @param coreRole 角色实体
	 * @return 是否新增成功
	 */
	Boolean add(CoreRole coreRole);

	/**
	 * 批量删除角色
	 *
	 * @author mr.g
	 * @param ids 角色ID列表
	 * @return 是否删除成功
	 */
	boolean removeByIds(List<Long> ids);

	/**
	 * 修改角色信息
	 *
	 * @author mr.g
	 * @param coreRole 角色实体
	 * @return 是否修改成功
	 */
	@Override
	boolean updateById(CoreRole coreRole);

	/**
	 * 保存顶部菜单关联信息
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 * @param menuIds 顶部菜单ID列表
	 * @return 是否保存成功
	 */
	boolean saveTopMenu(Long roleId, List<Long> menuIds);

	/**
	 * 获取角色树形结构
	 *
	 * @author mr.g
	 * @return 角色树形结构
	 */
	List<Tree<Long>> listTree(Map<String, Object> params);

	/**
	 * 获取角色树形结构
	 *
	 * @author mr.g
	 * @return 角色树形结构
	 */
	List<Tree<Long>> treeSelect();

	/**
	 * 根据角色ID查询顶级菜单ID
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 * @return 菜单ID列表
	 */
	List<Long> queryMenuIdByRoleId(Long roleId);

	/**
	 * 根据角色ID查询角色名称
	 *
	 * @author mr.g
	 * @param roleIds 角色ID列表
	 * @return 角色名称列表
	 */
	List<String> getRoleNameByIds(List<Long> roleIds);
}
