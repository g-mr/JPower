package com.wlcb.jpower.module.common.service.core.city;


import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;

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
    List<Map<String, Object>> listChild(Map<String, Object> city);

    /**
     * @Author 郭丁志
     * @Description //TODO 查询行政取区域列表
     * @Date 11:16 2020-07-23
     * @Param [coreCity]
     * @return java.util.List<com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity>
     *
     * @param coreCity*/
    List<TbCoreCity> list(TbCoreCity coreCity);
}
