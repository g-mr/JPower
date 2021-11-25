package com.wlcb.jpower.service.dict;

import cn.hutool.core.lang.tree.Tree;
import com.wlcb.jpower.dbs.entity.dict.TbCoreDictType;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreDictTypeService extends BaseService<TbCoreDictType> {

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典类型树形结构
     * @date 18:24 2020/7/26 0026
     * @param
     * @return java.util.List<com.wlcb.jpower.module.common.node.Node>
     */
    List<Tree<String>> tree();

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典类型树形列表结构
     * @date 18:24 2020/7/26 0026
     */
    List<Tree<String>> listTree(TbCoreDictType dictType);

    /**
     * @author 郭丁志
     * @Description //TODO 批量删除字典类型
     * @date 19:23 2020/7/26 0026
     * @param ids
     * @return java.lang.Boolean
     */
    Boolean deleteDictType(List<String> ids);

    /**
     * @author 郭丁志
     * @Description //TODO 保存或者新增字典类型
     * @date 19:50 2020/7/26 0026
     * @param dictType
     * @return java.lang.Boolean
     */
    Boolean addDictType(TbCoreDictType dictType);

    /**
     * @author 郭丁志
     * @Description //TODO 修改字典类型
     * @date 22:53 2020/8/21 0021
     * @param dictType
     * @return java.lang.Boolean
     */
    Boolean updateDictType(TbCoreDictType dictType);

}
