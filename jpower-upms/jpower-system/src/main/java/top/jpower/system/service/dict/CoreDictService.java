package top.jpower.system.service.dict;


import top.jpower.system.dbs.entity.dict.CoreDict;
import top.jpower.core.dbs.service.BaseService;
import top.jpower.system.vo.DictVo;

import java.util.List;
import java.util.Map;

/**
 * 字典服务接口
 * 
 * @author mr.g
 */
public interface CoreDictService extends BaseService<CoreDict> {

    /**
     * 根据code查询详情
     * 
     * @author mr.g
     * @param dictTypeCode 字典类型编码
     * @param code 字典编码
     * @return 字典实体
     */
    CoreDict queryDictTypeByCode(String dictTypeCode, String code);

    /**
     * 保存或者修改字典
     * 
     * @author mr.g
     * @param dict 字典实体
     * @return 是否保存成功
     */
    Boolean saveDict(CoreDict dict);

    /**
     * 查询字典列表
     * 
     * @author mr.g
     * @param dict 查询条件
     * @return 字典视图列表
     */
    List<DictVo> listByType(CoreDict dict);

    /**
     * 通过字典类型查询字典列表
     * 
     * @author mr.g
     * @param dictTypeCode 字典类型编码
     * @return 字典列表
     */
    List<Map<String, Object>> listByTypeCode(String dictTypeCode);
}
