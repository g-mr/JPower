package com.wlcb.jpower.module.common.service.core.city;


import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
public interface CoreCityService {

    /**
     * @Author 郭丁志
     * @Description //TODO 通过code查询下级元素
     * @Date 09:59 2020-07-16
     * @Param [code]
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.String>>
     **/
    List<Map<String, Object>> listChild(String code);

}
