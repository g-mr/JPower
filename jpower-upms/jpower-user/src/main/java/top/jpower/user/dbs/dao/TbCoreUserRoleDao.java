package top.jpower.user.dbs.dao;


import org.springframework.stereotype.Repository;
import top.jpower.user.dbs.dao.mapper.TbCoreUserRoleMapper;
import top.jpower.jpower.dbs.entity.TbCoreUserRole;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.gmac
 */
@Repository
public class TbCoreUserRoleDao extends JpowerServiceImpl<TbCoreUserRoleMapper, TbCoreUserRole> {
}
