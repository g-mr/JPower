package top.jpower.jpower.dbs.dao.org.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.jpower.core.dbs.dbs.dao.mapper.base.JpowerBaseMapper;
import top.jpower.jpower.dbs.entity.org.TbCoreOrg;
import top.jpower.jpower.vo.OrgVo;

import java.util.List;

/**
 * @author mr.gmac
 */
@Mapper
public interface TbCoreOrgMapper extends JpowerBaseMapper<TbCoreOrg> {

    /**
     * @author 郭丁志
     * @Description //TODO 根据父级加载部门列表
     * @date 0:41 2020/8/22 0022
     * @param coreOrg
     * @return java.util.List<top.jpower.jpower.module.dbs.vo.OrgVo>
     */
    List<OrgVo> listLazyByParent(@Param("org") TbCoreOrg coreOrg);

}
