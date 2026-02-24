package top.jpower.system.service.city.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.system.dbs.dao.city.CoreCityDao;
import top.jpower.system.dbs.dao.city.mapper.CoreCityMapper;
import top.jpower.system.dbs.entity.city.CoreCity;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.dbs.mp.support.Condition;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.system.service.city.CoreCityService;
import top.jpower.system.vo.CityVo;

import java.util.List;
import java.util.Map;

/**
 * 城市服务实现
 * 
 * @author mr.g
 */
@Service
@AllArgsConstructor
public class CoreCityServiceImpl extends BaseServiceImpl<CoreCityMapper, CoreCity> implements CoreCityService {

    private CoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listChild(Map<String, Object> city) {
        return coreCityDao.listMaps(Condition.getQueryWrapper(city,CoreCity.class).lambda()
                .select(CoreCity::getCode,CoreCity::getName)
                .orderByAsc(CoreCity::getSortNum));
    }

    @Cacheable(value = CacheNames.CITY_PARENT_LIST_REDIS_KEY,key = "#coreCity.toString()" +
            ".concat(T(top.jpower.core.dbs.page.PaginationContext).pageNum)" +
            ".concat(T(top.jpower.core.dbs.page.PaginationContext).pageSize)" +
            ".concat(T(top.jpower.core.dbs.page.PaginationContext).asc)" +
            ".concat(T(top.jpower.core.dbs.page.PaginationContext).desc)")
    @Override
    public List<CoreCity> list(CoreCity coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity).lambda().orderByAsc(CoreCity::getSortNum));
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public boolean add(CoreCity coreCity) {
        coreCity.setFullname(Fc.isNotBlank(coreCity.getFullname())?coreCity.getFullname():coreCity.getName());
        coreCity.setCountryCode(Fc.isNotBlank(coreCity.getCountryCode())?coreCity.getCountryCode():JpowerConstants.COUNTRY_CODE);
        coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

        JpowerAssert.notTrue(queryByCode(coreCity.getCode()) != null, JpowerError.Business,"该编号已存在");

        // 新增的下级如果是第一个，则需要删除上级的缓存
        try {
            if (coreCityDao.count(Condition.<CoreCity>getQueryWrapper().lambda().eq(CoreCity::getPcode,coreCity.getPcode())) <= 0){
                CoreCity city = queryByCode(coreCity.getPcode());
                if (Fc.notNull(city)){
                    CacheUtil.remove(CacheNames.CITY_PARENT_REDIS_KEY, city.getPcode());
                }
            }
        }catch (Exception e){
            log.warn("("+CacheNames.CITY_PARENT_REDIS_KEY+")缓存删除失败："+e.getMessage());
        }

        CacheUtil.clear(CacheNames.CITY_KEY);
        return coreCityDao.save(coreCity);
    }

    @Override
    public CoreCity queryByCode(String cityCode) {
        return coreCityDao.getOne(Condition.<CoreCity>getQueryWrapper().lambda().eq(CoreCity::getCode,cityCode));
    }

    @Override
    @CacheEvict(value = {CacheNames.CITY_PARENT_REDIS_KEY,CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY},allEntries = true)
    public Boolean deleteBatch(List<Long> ids) {

        List<Object> listCode = coreCityDao.listObjs(Condition.<CoreCity>getQueryWrapper().lambda().select(CoreCity::getCode).in(CoreCity::getId,ids));
        if(listCode.size()>0){
            long count = coreCityDao.count(Condition.<CoreCity>getQueryWrapper().lambda().in(CoreCity::getPcode,listCode));
            JpowerAssert.geZero(count,JpowerError.Business,"请先删除子区域");
        }

        CacheUtil.clear(CacheNames.CITY_KEY);
        return coreCityDao.removeRealByIds(ids);
    }

    @Override
    @Cacheable(value = CacheNames.CITY_PARENT_REDIS_KEY,key = "#pcode ?: '-1'")
    public List<Tree<String>> lazyTree(String pcode) {
        return coreCityDao.tree(Condition.getLambdaTreeWrapper(CoreCity.class,CoreCity::getCode,CoreCity::getPcode)
                .lazy(pcode).unLambda().select("sort_num AS sort","fullname AS name","id AS `key`"));
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public Boolean update(CoreCity coreCity) {
        CoreCity city = coreCityDao.getById(coreCity.getId());
        if (!StringUtil.equals(city.getCode(),coreCity.getCode())){
            JpowerAssert.isNull(queryByCode(coreCity.getCode()),JpowerError.Business,"编号已存在");
        }

        boolean is = coreCityDao.updateById(coreCity);
        if (is && Fc.isNotBlank(coreCity.getCode()) && !Fc.isNull(city) && !StringUtil.equals(city.getCode(),coreCity.getCode())){
            coreCityDao.update(new UpdateWrapper<CoreCity>().lambda()
                    .set(CoreCity::getPcode,coreCity.getCode())
                    .eq(CoreCity::getPcode,city.getCode()));
        }

        CacheUtil.clear(CacheNames.CITY_KEY);
        return is;
    }

    @Override
    public CityVo getById(Long id) {
        return coreCityDao.conver(super.getById(id));
    }

}
