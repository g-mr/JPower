package com.wlcb.jpower.module.dbs.dao.core.user.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;
import com.wlcb.jpower.module.dbs.vo.OrgVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("tbCoreOrgMapper")
public interface TbCoreOrgMapper extends BaseMapper<TbCoreOrg> {

    /**
     * @author 郭丁志
     * @Description //TODO 根据父级加载部门列表
     * @date 0:41 2020/8/22 0022
     * @param coreOrg
     * @return java.util.List<com.wlcb.jpower.module.dbs.vo.OrgVo>
     */
    List<OrgVo> listLazyByParent(@Param("org") TbCoreOrg coreOrg);

}
