package com.wlcb.jpower.service.org;

import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.entity.org.TbCoreOrg;
import com.wlcb.jpower.module.common.service.BaseService;
import com.wlcb.jpower.vo.OrgVo;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreOrgService extends BaseService<TbCoreOrg> {
    
    /**
     * @Author 郭丁志
     * @Description //TODO 通过上级节点查询下级节点
     * @Date 17:31 2020-05-20
     * @Param [coreOrg]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.user.TbCoreOrg>
     **/
    List<OrgVo> listLazyByParent(TbCoreOrg coreOrg);

    /**
     * @Author 郭丁志
     * @Description //TODO 新增组织机构
     * @Date 17:34 2020-05-20
     * @Param [coreOrg]
     * @return java.lang.Integer
     **/
    Boolean add(TbCoreOrg coreOrg);

    /**
     * @Author 郭丁志
     * @Description //TODO 批量查询下级节点的数量
     * @Date 17:35 2020-05-20
     * @Param [ids]
     * @return java.lang.Integer
     **/
    long listOrgByPids(String ids);

    /**
     * @author 郭丁志
     * @Description //TODO 更新组织机构
     * @date 0:01 2020/5/24 0024
     * @param coreOrg
     * @return java.lang.Integer
     */
    Boolean update(TbCoreOrg coreOrg);

    /**
     * @author 郭丁志
     * @Description //TODO 加载树形组织机构
     * @date 22:14 2020/7/26 0026
     * @param coreOrg
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Tree<String>> tree(Map<String, Object> coreOrg);

    /**
     * @author 郭丁志
     * @Description TODO 懒加载树形组织机构
     * @date 22:27 2020/7/26 0026
     * @param parentCode
     * @param coreOrg
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Tree<String>> tree(String parentCode, Map<String, Object> coreOrg);

    /**
     * @author 郭丁志
     * @Description //TODO 查子集
     * @date 1:52 2020/9/4 0004
     * @param id
     * @return java.util.List<java.lang.String>
     */
    List<String> queryChildById(String id);

}
