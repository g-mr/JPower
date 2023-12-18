package top.jpower.jpower.dbs.dao.role;


import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.role.mapper.TbCoreRoleMapper;
import top.jpower.jpower.dbs.entity.role.TbCoreRole;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreRoleDao extends JpowerServiceImpl<TbCoreRoleMapper, TbCoreRole> {
}
