package com.qidiangk.smart.system.service.city.impl;

import cn.hutool.core.lang.tree.Tree;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.redis.cache.CacheUtil;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import com.qidiangk.smart.system.dbs.dao.city.CoreCityDao;
import com.qidiangk.smart.system.dbs.dao.city.mapper.CoreCityMapper;
import com.qidiangk.smart.system.dbs.entity.city.CoreCity;
import com.qidiangk.smart.system.service.city.CoreCityService;
import com.qidiangk.smart.system.vo.CityVO;
import com.qidiangk.smart.system.vo.SelectVO;

import java.util.List;

import static com.qidiangk.smart.common.constants.ServiceCodeConstants.CODE_EXIST;
import static com.qidiangk.smart.common.constants.ServiceCodeConstants.DELETE_CHILD;

/**
 * 城市服务实现
 * 
 * @author mr.g
 */
@Slf4j
@Service
@AllArgsConstructor
public class CoreCityServiceImpl extends BaseServiceImpl<CoreCityMapper, CoreCity> implements CoreCityService {

    private CoreCityDao coreCityDao;

    @Override
	@Cacheable(value = CacheNames.CITY_PARENT_CODE_REDIS_KEY,key = "#pcode.concat(#name==null ? '' : ':'+name)")
    public List<SelectVO> listChild(String pcode, String name) {
        return coreCityDao.listCodeName(pcode, name);
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
	@CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public boolean add(CoreCity coreCity) {
        coreCity.setFullname(Fc.toStr(coreCity.getFullname(), coreCity.getName()));
        coreCity.setCountryCode(Fc.toStr(coreCity.getCountryCode(), JpowerConstants.COUNTRY_CODE));
        coreCity.setPcode(Fc.toStr(coreCity.getPcode(), JpowerConstants.TOP_CODE));

        JpowerAssert.notTrue(queryByCode(coreCity.getCode()) != null, JpowerError.Business,CODE_EXIST);

        // 新增的下级如果是第一个，则需要删除上级的缓存
        try {
            if (!coreCityDao.existsByField(CoreCity::getPcode, coreCity.getPcode())){
                CoreCity city = queryByCode(coreCity.getPcode());
                if (Fc.notNull(city)){
                    CacheUtil.remove(CacheNames.CITY_PARENT_REDIS_KEY, city.getPcode());
                }
            }
        }catch (Exception e){
            log.warn("("+CacheNames.CITY_PARENT_REDIS_KEY+")缓存删除失败===>>"+e.getMessage());
        }

        CacheUtil.clear(CacheNames.CITY_KEY);
        return coreCityDao.save(coreCity);
    }

    @Override
    public CoreCity queryByCode(String cityCode) {
        return coreCityDao.getOneByField(CoreCity::getCode, cityCode);
    }

    @Override
    @CacheEvict(value = {CacheNames.CITY_PARENT_REDIS_KEY,CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY},allEntries = true)
    public Boolean deleteBatch(List<Long> ids) {
		if (ids.size() == 1) {
			Long id = ids.get(0);
			CoreCity city = coreCityDao.get(id);
			return coreCityDao.removeReal(Wrappers.getQueryWrapper().likeLeft(CoreCity::getCode, StringUtil.removeAllSuffix(city.getCode(), "0")));
		} else {
			List<String> listCode = coreCityDao.listCodeByIds(ids);
			if(listCode.size()>0){
				long count = coreCityDao.countInField(CoreCity::getPcode, listCode);
				JpowerAssert.geZero(count,JpowerError.Business,DELETE_CHILD);
			}

			CacheUtil.clear(CacheNames.CITY_KEY);
			return coreCityDao.removeRealByIds(ids);
		}
    }

    @Override
    @Cacheable(value = CacheNames.CITY_PARENT_REDIS_KEY,key = "#pcode ?: '-1'")
    public List<Tree<String>> lazyTree(String pcode) {
        return coreCityDao.lazyTree(pcode);
    }

    @Override
    @Caching(evict = {@CacheEvict(value= CacheNames.CITY_PARENT_REDIS_KEY, key = "#coreCity.pcode ?: '-1'"),
	@CacheEvict(value= {CacheNames.CITY_PARENT_LIST_REDIS_KEY,CacheNames.CITY_PARENT_CODE_REDIS_KEY}, allEntries = true)})
    public Boolean update(CoreCity coreCity) {
        CoreCity city = coreCityDao.getById(coreCity.getId());
        if (!StringUtil.equals(city.getCode(), coreCity.getCode())){
            JpowerAssert.isNull(queryByCode(coreCity.getCode()), JpowerError.Business,CODE_EXIST);
        }

        boolean is = coreCityDao.updateById(coreCity);
        if (is && Fc.isNotBlank(coreCity.getCode()) && !Fc.isNull(city) && !StringUtil.equals(city.getCode(),coreCity.getCode())){
			// 更新下级的上级CODE
            coreCityDao.updatePcodeByCode(coreCity.getPcode(), city.getCode());
        }

        CacheUtil.clear(CacheNames.CITY_KEY);
        return is;
    }

    @Override
    public CityVO getById(Long id) {
        return coreCityDao.get(id);
    }

}
