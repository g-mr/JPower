package top.jpower.system.dbs.dao.role;

import com.mybatisflex.core.util.UpdateEntity;
import org.springframework.stereotype.Repository;
import top.jpower.common.enums.YN01Enum;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.utils.Fc;
import top.jpower.system.dbs.dao.role.mapper.CoreTopMenuMapper;
import top.jpower.system.dbs.entity.function.CoreTopMenu;
import top.jpower.system.dbs.entity.role.CoreRoleMenu;
import top.jpower.system.vo.MenuClientVO;
import top.jpower.system.vo.MenuVO;

import java.util.List;

/**
 * 顶部菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreTopMenuDao extends JpowerServiceImpl<CoreTopMenuMapper, CoreTopMenu> {

	/**
	 * 根据编号和去除的ID判断是否存在
	 *
	 * @param code 编码
	 * @return 是否存在
	 */
	public boolean existsByCodeNeId(String code, Long id) {
		return super.exists(Wrappers.getQueryWrapper().eq(CoreTopMenu::getCode, code).ne(CoreTopMenu::getId, id));
	}

	/**
	 * 启用或禁用
	 *
	 * @param id ID
	 * @param status 状态
	 * @return 是否成功
	 */
	public boolean switchById(Long id, Boolean status) {
		return super.updateById(UpdateEntity.of(CoreTopMenu.class).setId(id).setStatus(status));
	}

	/**
	 * 获取角色菜单
	 *
	 * @param clientId 客户端ID
	 * @param roleIds 角色ID列表
	 * @return 角色菜单
	 */
	public List<MenuClientVO> selectList(Long clientId, List<Long> roleIds) {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CoreTopMenu::getId, CoreTopMenu::getName, CoreTopMenu::getClientId)
				.leftJoin(CoreRoleMenu.class, Fc.isNotEmpty(roleIds)).on(CoreRoleMenu::getMenuId, CoreTopMenu::getId)
				.eq(CoreTopMenu::getClientId, clientId, Fc.notNull(clientId))
				.in(CoreRoleMenu::getRoleId, roleIds, Fc.isNotEmpty(roleIds)),
				MenuClientVO.class);
	}

	/**
	 * 获取菜单
	 *
	 * @param clientId 客户端ID
	 * @param roleIds 角色ID列表
	 * @return 菜单
	 */
	public List<MenuVO> listMenuByClientIdRoleId(Long clientId, List<Long> roleIds) {
		return super.listAs(Wrappers.getQueryWrapper()
				.select(CoreTopMenu::getId, CoreTopMenu::getName, CoreTopMenu::getCode, CoreTopMenu::getIcon, CoreTopMenu::getRouter)
				.eq(CoreTopMenu::getStatus, YN01Enum.Y.getValue())
				.eq(CoreTopMenu::getClientId, clientId)
				.leftJoin(CoreRoleMenu.class, Fc.isNotEmpty(roleIds)).on(CoreRoleMenu::getMenuId, CoreTopMenu::getId)
				.in(CoreRoleMenu::getRoleId, roleIds, Fc.isNotEmpty(roleIds))
				.orderBy(CoreTopMenu::getSortNum).asc(), MenuVO.class);
	}
}
