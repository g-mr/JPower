package top.jpower.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreDataScopeMapper;
import top.jpower.system.dbs.entity.function.CoreDataScope;

/**
 * 数据权限数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreDataScopeDao extends JpowerServiceImpl<CoreDataScopeMapper, CoreDataScope> {
}
