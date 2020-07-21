package com.wlcb.jpower.module.common.service.core.dict;


import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType;

/**
 * @author mr.gmac
 */
public interface CoreDictService {

    /**
     * @Author 郭丁志
     * @Description //TODO 根据code查询详情
     * @Date 15:59 2020-07-13
     * @Param [dictTypeCode, code]
     * @return com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDictType
     **/
    public TbCoreDict queryDictTypeByCode(String dictTypeCode, String code);

}
