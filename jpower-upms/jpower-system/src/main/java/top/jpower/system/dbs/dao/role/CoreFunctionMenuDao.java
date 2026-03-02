package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.system.dbs.dao.role.mapper.CoreFunctionMenuMapper;
import top.jpower.system.dbs.entity.function.CoreFunctionMenu;

import java.util.List;

/**
 * 功能菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreFunctionMenuDao extends JpowerServiceImpl<CoreFunctionMenuMapper, CoreFunctionMenu> {

	/**
	 * 根据菜单ID查询功能ID
	 *
	 * @param menuId 菜单ID
	 * @return 功能ID列表
	 */
	public List<Long> listFunctionId(Long menuId) {
		return super.objListAs(Wrappers.getQueryWrapper()
				.select(CoreFunctionMenu::getFunctionId)
				.eq(CoreFunctionMenu::getMenuId, menuId), Long.class);
	}

	/**
	 * 根据菜单ID删除
	 *
	 * @param menuId 菜单ID
	 */
	public void removeByMenuId(Long menuId) {
		super.removeReal(Wrappers.getQueryWrapper().eq(CoreFunctionMenu::getMenuId, menuId));
	}
}
