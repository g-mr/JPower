package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleMenuMapper;
import top.jpower.system.dbs.entity.role.CoreRoleMenu;

import java.util.List;

/**
 * 角色菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleMenuDao extends JpowerServiceImpl<CoreRoleMenuMapper,CoreRoleMenu> {

	/**
	 * 根据角色ID删除
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 **/
	public void removeRealByRoleId(Long roleId) {
		super.removeReal(Wrappers.getQueryWrapper().eq(CoreRoleMenu::getRoleId, roleId));
	}

	/**
	 * 根据角色ID查询顶级菜单ID
	 *
	 * @author mr.g
	 * @param roleId 角色ID
	 * @return 菜单ID列表
	 **/
	public List<Long> queryMenuIdByRoleId(Long roleId) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreRoleMenu::getMenuId)
				.eq(CoreRoleMenu::getRoleId, roleId), Long.class);
	}
}
