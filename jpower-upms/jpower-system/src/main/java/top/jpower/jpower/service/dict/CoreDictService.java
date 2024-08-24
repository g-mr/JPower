package top.jpower.jpower.service.dict;


import top.jpower.jpower.dbs.entity.dict.TbCoreDict;
import top.jpower.jpower.module.service.BaseService;
import top.jpower.jpower.vo.DictVo;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreDictService extends BaseService<TbCoreDict> {

    /**
     * @Author 郭丁志
     * @Description //TODO 根据code查询详情
     * @Date 15:59 2020-07-13
     * @Param [dictTypeCode, code]
     * @return top.jpower.jpower.module.dbs.entity.core.dict.TbCoreDictType
     **/
    TbCoreDict queryDictTypeByCode(String dictTypeCode, String code);

    /**
     * @author 郭丁志
     * @Description //TODO 保存或者修改字典
     * @date 20:59 2020/7/26 0026
     * @param dict
     * @return java.lang.Boolean
     */
    Boolean saveDict(TbCoreDict dict);

    /**
     * @author 郭丁志
     * @Description //TODO 查询字典列表
     * @date 22:13 2020/8/21 0021
     * @param dict 
     * @return java.util.List<top.jpower.jpower.module.dbs.vo.DictVo>
     */
    List<DictVo> listByType(TbCoreDict dict);

    /**
     * @Author 郭丁志
     * @Description //TODO 通过字典类型查询字典列表
     * @Date 17:34 2020-10-22
     * @Param [dictTypeCode]
     **/
    List<Map<String, Object>> listByTypeCode(String dictTypeCode);
}
