package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreTopMenuMapper;
import top.jpower.system.dbs.entity.function.CoreTopMenu;

/**
 * 顶部菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreTopMenuDao extends JpowerServiceImpl<CoreTopMenuMapper, CoreTopMenu> {
}
