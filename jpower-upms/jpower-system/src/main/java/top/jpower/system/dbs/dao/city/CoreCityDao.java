package top.jpower.system.dbs.dao.city;

import org.springframework.stereotype.Repository;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.system.api.cache.SystemCache;
import top.jpower.system.dbs.dao.city.mapper.CoreCityMapper;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.core.dbs.dbs.dao.BaseDaoWrapper;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.system.vo.CityVo;

import java.util.Objects;

/**
 * 城市数据访问对象
 * 
 * @author mr.g
 */
@Repository
public class CoreCityDao extends JpowerServiceImpl<CoreCityMapper, CoreCity> implements BaseDaoWrapper<CoreCity> {

    public CityVo conver(CoreCity city) {
        CityVo cityVo = Objects.requireNonNull(BeanUtil.copyProperties(city, CityVo.class));
        cityVo.setPname(SystemCache.getCityName(city.getPcode()));
        return cityVo;
    }
}
