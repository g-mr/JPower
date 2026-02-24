package top.jpower.system.dbs.dao.role;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.dbs.dao.role.mapper.CoreRoleDataMapper;
import top.jpower.system.dbs.entity.role.CoreRoleData;

/**
 * 角色数据权限数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreRoleDataDao extends JpowerServiceImpl<CoreRoleDataMapper, CoreRoleData> {
}
