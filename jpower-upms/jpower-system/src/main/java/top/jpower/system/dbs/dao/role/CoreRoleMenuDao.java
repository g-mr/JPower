package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleMenuMapper;
import top.jpower.system.dbs.entity.role.CoreRoleMenu;

/**
 * 角色菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleMenuDao extends JpowerServiceImpl<CoreRoleMenuMapper,CoreRoleMenu> {
}
