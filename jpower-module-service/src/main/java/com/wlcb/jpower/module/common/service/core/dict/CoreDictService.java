package com.wlcb.jpower.module.common.service.core.dict;


import com.wlcb.jpower.module.common.service.base.BaseService;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;

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
}
