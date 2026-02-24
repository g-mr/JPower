package top.jpower.system.service.dict;

import cn.hutool.core.lang.tree.Tree;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.dbs.entity.dict.CoreDictType;

import java.util.List;

/**
 * 字典类型服务接口
 * 
 * @author mr.g
 */
public interface CoreDictTypeService extends BaseService<CoreDictType> {

    /**
     * 查询字典类型树形结构
     * 
     * @author mr.g
     * @return 字典类型树形列表
     */
    List<Tree<Long>> tree();

    /**
     * 批量删除字典类型
     * 
     * @author mr.g
     * @param ids ID列表
     * @return 是否删除成功
     */
    Boolean deleteDictType(List<Long> ids);

    /**
     * 保存或者新增字典类型
     * 
     * @author mr.g
     * @param dictType 字典类型实体
     * @return 是否保存成功
     */
    Boolean addDictType(CoreDictType dictType);

    /**
     * 修改字典类型
     * 
     * @author mr.g
     * @param dictType 字典类型实体
     * @return 是否修改成功
     */
    Boolean updateDictType(CoreDictType dictType);

}
