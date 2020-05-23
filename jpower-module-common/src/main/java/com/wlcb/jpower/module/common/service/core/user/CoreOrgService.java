package com.wlcb.jpower.module.common.service.core.user;

import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg;

import java.util.List;

public interface CoreOrgService {
    
    /**
     * @Author 郭丁志
     * @Description //TODO 通过上级节点查询下级节点
     * @Date 17:31 2020-05-20
     * @Param [coreOrg]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg>
     **/
    List<TbCoreOrg> listByParent(TbCoreOrg coreOrg);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询组织机构
     * @Date 17:33 2020-05-20
     * @Param [code]
     * @return com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg
     **/
    TbCoreOrg selectOrgByCode(String code);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增组织机构
     * @Date 17:34 2020-05-20
     * @Param [coreOrg]
     * @return java.lang.Integer
     **/
    Integer add(TbCoreOrg coreOrg);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量查询下级节点的数量
     * @Date 17:35 2020-05-20
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Integer listOrgByPids(String ids);

    /**
     * @Author 郭丁志
     * @Description //TODO 删除组织机构
     * @Date 17:36 2020-05-20
     * @Param [ids]
     * @return java.lang.Integer
     **/
    Integer delete(String ids);

    /**
     * @author 郭丁志
     * @Description //TODO 更新组织机构
     * @date 0:01 2020/5/24 0024
     * @param coreOrg
     * @return java.lang.Integer
     */
    Integer update(TbCoreOrg coreOrg);
}
