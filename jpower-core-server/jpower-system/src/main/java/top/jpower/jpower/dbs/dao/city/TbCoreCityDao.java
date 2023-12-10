package top.jpower.jpower.dbs.dao.city;

import top.jpower.jpower.cache.SystemCache;
import top.jpower.jpower.dbs.dao.city.mapper.TbCoreCityMapper;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.jpower.module.common.utils.BeanUtil;
import top.jpower.jpower.module.dbs.dao.BaseDaoWrapper;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import top.jpower.jpower.vo.CityVo;
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
        CityVo cityVo = Objects.requireNonNull(BeanUtil.copyProperties(city, CityVo.class));
        cityVo.setPname(SystemCache.getCityName(city.getPcode()));
        return cityVo;
    }
}
