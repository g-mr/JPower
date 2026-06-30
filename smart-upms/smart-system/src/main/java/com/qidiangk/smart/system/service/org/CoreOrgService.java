package com.qidiangk.smart.system.service.org;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import com.qidiangk.smart.system.dbs.entity.org.CoreOrg;
import com.qidiangk.smart.system.vo.OrgVO;

import java.util.List;
import java.util.Map;

/**
 * 组织机构服务接口
 * 
 * @author mr.g
 */
public interface CoreOrgService extends BaseService<CoreOrg> {
    
    /**
     * 通过上级节点查询下级节点
     * 
     * @author mr.g
     * @param map 查询条件
     * @return 组织机构视图列表
     */
    List<OrgVO> listLazyByParent(Map<String, Object> map);

	/**
     * 分页懒加载组织机构树形列表
     *
     * @param map 查询条件
     * @return 组织机构树形列表
     */
	List<OrgVO> pageTop(Map<String, Object> map);

    /**
     * 新增组织机构
     * 
     * @author mr.g
     * @param coreOrg 组织机构实体
     * @return 是否新增成功
     */
    Long create(CoreOrg coreOrg);

    /**
     * 批量查询下级节点的数量
     * 
     * @author mr.g
     * @param ids ID列表
     * @return 下级节点数量
     */
    long countByParentids(List<Long> ids);

    /**
     * 更新组织机构
     * 
     * @author mr.g
     * @param coreOrg 组织机构实体
     * @return 是否更新成功
     */
    Boolean update(CoreOrg coreOrg);

    /**
     * 加载树形组织机构
     * 
     * @author mr.g
     * @param coreOrg 查询条件
     * @return 组织机构树形列表
     */
    List<Tree<Long>> tree(Map<String, Object> coreOrg);

    /**
     * 懒加载树形组织机构
     * 
     * @author mr.g
     * @param parentCode 父级编码
     * @param map 查询条件
     * @return 组织机构树形列表
     */
    List<Tree<Long>> tree(Long parentCode, Map<String, Object> map);

    /**
     * 查子集
     * 
     * @author mr.g
     * @param id ID
     * @return 子集ID列表
     */
    List<Long> queryChildIdById(Long id);

}
