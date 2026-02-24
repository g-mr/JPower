package top.jpower.system.dbs.dao.org;


import org.springframework.stereotype.Repository;
import top.jpower.system.dbs.dao.org.mapper.CoreOrgMapper;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * 组织机构数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreOrgDao extends JpowerServiceImpl<CoreOrgMapper, CoreOrg> {
}
