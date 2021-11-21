package com.wlcb.jpower.dbs.dao.org.mapper;


import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.dbs.dao.mapper.base.JpowerBaseMapper;
import com.wlcb.jpower.vo.OrgVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreOrgMapper")
public interface TbCoreOrgMapper extends JpowerBaseMapper<TbCoreOrg> {

    /**
     * @author 郭丁志
     * @Description //TODO 根据父级加载部门列表
     * @date 0:41 2020/8/22 0022
     * @param coreOrg
     * @return java.util.List<com.wlcb.jpower.module.dbs.vo.OrgVo>
     */
    List<OrgVo> listLazyByParent(@Param("org") TbCoreOrg coreOrg);

}
