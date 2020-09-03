package com.wlcb.jpower.service.city.impl;

import com.wlcb.jpower.dbs.dao.city.TbCoreCityDao;
import com.wlcb.jpower.dbs.dao.city.mapper.TbCoreCityMapper;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.node.Node;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.RandomUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.city.CoreCityService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
@AllArgsConstructor
public class CoreCityServiceImpl extends BaseServiceImpl<TbCoreCityMapper, TbCoreCity> implements CoreCityService {

    private TbCoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listChild(Map<String, Object> city) {
        return coreCityDao.listMaps(Condition.getQueryWrapper(city,TbCoreCity.class).lambda()
                .select(TbCoreCity::getCode,TbCoreCity::getName)
                .orderByAsc(TbCoreCity::getSortNum));
    }

    @Cacheable(value = CacheNames.CITY_PARENT_LIST_REDIS_KEY,key = "#coreCity.toString()" +
            ".concat(T(com.wlcb.jpower.module.common.page.PaginationContext).pageNum)" +
            ".concat(T(com.wlcb.jpower.module.common.page.PaginationContext).pageSize)" +
            ".concat(T(com.wlcb.jpower.module.common.page.PaginationContext).asc)" +
            ".concat(T(com.wlcb.jpower.module.common.page.PaginationContext).desc)")
    @Override
    public List<TbCoreCity> list(TbCoreCity coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public boolean add(TbCoreCity coreCity) {
        coreCity.setCode(RandomUtil.createCityCode(coreCity.getPcode(),coreCity.getCode()));

        TbCoreCity city = queryByCode(coreCity.getCode());

        coreCity.setFullname(Fc.isNotBlank(coreCity.getFullname())?coreCity.getFullname():coreCity.getName());
        coreCity.setCountryCode(Fc.isNotBlank(coreCity.getCountryCode())?coreCity.getCountryCode():JpowerConstants.COUNTRY_CODE);
        coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

        JpowerAssert.notTrue(city != null, JpowerError.BUSINESS,"该编号已存在");

        return coreCityDao.save(coreCity);
    }

    @Override
    public TbCoreCity queryByCode(String cityCode) {
        return coreCityDao.getOne(Condition.<TbCoreCity>getQueryWrapper().lambda().eq(TbCoreCity::getCode,cityCode));
    }

    @Override
    @CacheEvict(value = {CacheNames.CITY_PARENT_REDIS_KEY,CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY},allEntries = true)
    public Boolean deleteBatch(List<String> ids) {

        List<Object> listCode = coreCityDao.listObjs(Condition.<TbCoreCity>getQueryWrapper().lambda().select(TbCoreCity::getCode).in(TbCoreCity::getId,ids));
        if(listCode.size()>0){
            Integer count = coreCityDao.count(Condition.<TbCoreCity>getQueryWrapper().lambda().in(TbCoreCity::getPcode,listCode));
            JpowerAssert.notTrue(count>0,JpowerError.BUSINESS,"请先删除子区域");
        }

        return coreCityDao.removeRealByIds(ids);
    }

    @Override
    @Cacheable(value = CacheNames.CITY_PARENT_REDIS_KEY,key = "#pcode")
    public List<Node> lazyTree(String pcode) {
        return coreCityDao.tree(Condition.getTreeWrapper(TbCoreCity::getCode,TbCoreCity::getPcode,TbCoreCity::getName)
                .lazy(pcode).lambda()
                .orderByAsc(TbCoreCity::getSortNum));
    }

}
