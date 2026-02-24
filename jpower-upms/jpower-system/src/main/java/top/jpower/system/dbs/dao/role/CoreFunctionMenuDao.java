package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreFunctionMenuMapper;
import top.jpower.system.dbs.entity.function.CoreFunctionMenu;

/**
 * 功能菜单数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreFunctionMenuDao extends JpowerServiceImpl<CoreFunctionMenuMapper, CoreFunctionMenu> {
}
