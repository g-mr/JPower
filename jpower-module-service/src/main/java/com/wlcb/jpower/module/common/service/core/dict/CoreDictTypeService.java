package com.wlcb.jpower.module.common.service.core.dict;

import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType;

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
    List<Node> tree();

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
    Boolean saveDictType(TbCoreDictType dictType);
}
