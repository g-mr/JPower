package top.jpower.system.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleMapper;
import top.jpower.system.dbs.entity.role.CoreRole;

/**
 * 角色数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleDao extends JpowerServiceImpl<CoreRoleMapper, CoreRole> {
}
