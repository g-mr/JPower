package com.wlcb.jpower.dbs.dao.city;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.dao.city.mapper.TbCoreCityMapper;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.dbs.dao.BaseDaoWrapper;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.vo.CityVo;
import org.springframework.stereotype.Repository;

import java.util.Objects;

/**
 * @ClassName TbCoreParamsDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 13:30
 * @Version 1.0
 */
@Repository
public class TbCoreCityDao extends JpowerServiceImpl<TbCoreCityMapper, TbCoreCity> implements BaseDaoWrapper<TbCoreCity,CityVo> {

    @Override
    public CityVo conver(TbCoreCity city) {
        CityVo cityVo = Objects.requireNonNull(BeanUtil.copy(city, CityVo.class));
        cityVo.setPname(SystemCache.getCityName(city.getPcode()));
        return cityVo;
    }
}
