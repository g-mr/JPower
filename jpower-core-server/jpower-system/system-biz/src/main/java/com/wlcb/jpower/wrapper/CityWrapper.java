package com.wlcb.jpower.wrapper;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.mp.support.BaseWrapper;
import com.wlcb.jpower.vo.CityVo;

import java.util.Objects;

/**
 * @author ding
 * @description
 * @date 2021-02-21 23:31
 */
public class CityWrapper extends BaseWrapper<TbCoreCity, CityVo> {

    public static CityWrapper builder(){
        return new CityWrapper();
    }

    @Override
    protected CityVo conver(TbCoreCity city) {
        CityVo cityVo = Objects.requireNonNull(BeanUtil.copyProperties(city, CityVo.class));
        cityVo.setPname(SystemCache.getCityName(city.getPcode()));
        return cityVo;
    }
}
