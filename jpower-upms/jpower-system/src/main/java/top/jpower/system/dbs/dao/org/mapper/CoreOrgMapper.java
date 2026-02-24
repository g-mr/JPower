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

    /**
     * 根据父级加载部门列表
     * 
     * @author mr.g
     * @param coreOrg 查询条件
     * @return java.util.List<top.jpower.system.vo.OrgVo> 部门列表
     */
    List<OrgVo> listLazyByParent(@Param("org") CoreOrg coreOrg);

}
