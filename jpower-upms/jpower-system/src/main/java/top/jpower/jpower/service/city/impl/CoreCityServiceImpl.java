package top.jpower.jpower.service.city.impl;

import cn.hutool.core.lang.tree.Tree;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dbs.dao.city.TbCoreCityDao;
import top.jpower.jpower.dbs.dao.city.mapper.TbCoreCityMapper;
import top.jpower.jpower.dbs.entity.city.TbCoreCity;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.handler.JpowerAssert;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.common.utils.CacheUtil;
import top.jpower.jpower.module.common.utils.Cm;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.city.CoreCityService;
import top.jpower.jpower.vo.CityVo;

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
            ".concat(T(top.jpower.jpower.module.common.page.PaginationContext).pageNum)" +
            ".concat(T(top.jpower.jpower.module.common.page.PaginationContext).pageSize)" +
            ".concat(T(top.jpower.jpower.module.common.page.PaginationContext).asc)" +
            ".concat(T(top.jpower.jpower.module.common.page.PaginationContext).desc)")
    @Override
    public List<TbCoreCity> list(TbCoreCity coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public boolean add(TbCoreCity coreCity) {
        coreCity.setFullname(Fc.isNotBlank(coreCity.getFullname())?coreCity.getFullname():coreCity.getName());
        coreCity.setCountryCode(Fc.isNotBlank(coreCity.getCountryCode())?coreCity.getCountryCode():JpowerConstants.COUNTRY_CODE);
        coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

        JpowerAssert.notTrue(queryByCode(coreCity.getCode()) != null, JpowerError.Business,"该编号已存在");

        // 新增的下级如果是第一个，则需要删除上级的缓存
        try {
            if (coreCityDao.count(Condition.<TbCoreCity>getQueryWrapper().lambda().eq(TbCoreCity::getPcode,coreCity.getPcode())) <= 0){
                TbCoreCity city = queryByCode(coreCity.getPcode());
                if (Fc.notNull(city)){
                    Fc.requireNotNull(Cm.getInstance().getCache(CacheNames.CITY_PARENT_REDIS_KEY, Boolean.FALSE),"缓存不存在").evict(city.getPcode());
                }
            }
        }catch (Exception e){
            log.warn("("+CacheNames.CITY_PARENT_REDIS_KEY+")缓存删除失败："+e.getMessage());
        }

        CacheUtil.clear(CacheNames.CITY_KEY,Boolean.FALSE);
        return coreCityDao.save(coreCity);
    }

    @Override
    public TbCoreCity queryByCode(String cityCode) {
        return coreCityDao.getOne(Condition.<TbCoreCity>getQueryWrapper().lambda().eq(TbCoreCity::getCode,cityCode));
    }

    @Override
    @CacheEvict(value = {CacheNames.CITY_PARENT_REDIS_KEY,CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY},allEntries = true)
    public Boolean deleteBatch(List<Long> ids) {

        List<Object> listCode = coreCityDao.listObjs(Condition.<TbCoreCity>getQueryWrapper().lambda().select(TbCoreCity::getCode).in(TbCoreCity::getId,ids));
        if(listCode.size()>0){
            long count = coreCityDao.count(Condition.<TbCoreCity>getQueryWrapper().lambda().in(TbCoreCity::getPcode,listCode));
            JpowerAssert.geZero(count,JpowerError.Business,"请先删除子区域");
        }

        CacheUtil.clear(CacheNames.CITY_KEY,Boolean.FALSE);
        return coreCityDao.removeRealByIds(ids);
    }

    @Override
    @Cacheable(value = CacheNames.CITY_PARENT_REDIS_KEY,key = "#pcode ?: '-1'")
    public List<Tree<String>> lazyTree(String pcode) {
        return coreCityDao.tree(Condition.getLambdaTreeWrapper(TbCoreCity.class,TbCoreCity::getCode,TbCoreCity::getPcode)
                .lazy(pcode).unLambda().select("sort_num AS sort","fullname AS name","id AS `key`"));
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
            @CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public Boolean update(TbCoreCity coreCity) {
        TbCoreCity city = coreCityDao.getById(coreCity.getId());
        if (!StringUtil.equals(city.getCode(),coreCity.getCode())){
            JpowerAssert.isNull(queryByCode(coreCity.getCode()),JpowerError.Business,"编号已存在");
        }

        boolean is = coreCityDao.updateById(coreCity);
        if (is && Fc.isNotBlank(coreCity.getCode()) && !Fc.isNull(city) && !StringUtil.equals(city.getCode(),coreCity.getCode())){
            coreCityDao.update(new UpdateWrapper<TbCoreCity>().lambda()
                    .set(TbCoreCity::getPcode,coreCity.getCode())
                    .eq(TbCoreCity::getPcode,city.getCode()));
        }

        CacheUtil.clear(CacheNames.CITY_KEY,Boolean.FALSE);
        return is;
    }

    @Override
    public CityVo getById(Long id) {
        return coreCityDao.conver(super.getById(id));
    }

}
