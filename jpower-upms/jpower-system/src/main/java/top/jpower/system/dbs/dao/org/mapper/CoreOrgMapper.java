package top.jpower.system.dbs.dao.org.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.system.dbs.entity.org.CoreOrg;
import top.jpower.system.vo.OrgVo;

import java.util.List;

/**
 * 组织机构数据访问映射器
 * 
 * @author mr.g
 */
@Mapper
public interface CoreOrgMapper extends JpowerBaseMapper<CoreOrg> {
}
