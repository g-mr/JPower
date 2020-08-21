package com.wlcb.jpower.module.common.service.core.dict;


import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.vo.DictVo;

import java.util.List;

/**
 * @author mr.gmac
 */
public interface CoreDictService extends BaseService<TbCoreDict> {

    /**
     * @Author 郭丁志
     * @Description //TODO 根据code查询详情
     * @Date 15:59 2020-07-13
     * @Param [dictTypeCode, code]
     * @return com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType
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
     * @return java.util.List<com.wlcb.jpower.module.dbs.vo.DictVo>
     */
    List<DictVo> listByType(TbCoreDict dict);
}
