package com.wlcb.jpower.module.common.service.core.city.impl;

import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.CodeUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.JpowerConstants;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.core.city.TbCoreCityDao;
import com.wlcb.jpower.module.dbs.dao.core.city.mapper.TbCoreCityMapper;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.module.mp.support.SqlKeyword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
public class CoreCityServiceImpl extends JpowerServiceImpl<TbCoreCityMapper,TbCoreCity> implements CoreCityService {

    /** 省级别 **/
    private final Integer PROVINCE_RANKD = 1;
    /** 市级别 **/
    private final Integer CITY_RANKD = 2;
    /** 区级别 **/
    private final Integer DISTRICT_RANKD = 3;
    /** 县级别 **/
    private final Integer TOWN_RANKD = 4;
    /** 乡村级别 **/
    private final Integer VILLAGE_RANKD = 5;

    @Autowired
    private TbCoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listChild(Map<String, Object> city) {
        return coreCityDao.listMaps(Condition.getQueryWrapper(city,TbCoreCity.class).lambda()
                .select(TbCoreCity::getCode,TbCoreCity::getName)
                .orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    public List<TbCoreCity> list(TbCoreCity coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    public boolean save(TbCoreCity coreCity) {
        coreCity.setCode(CodeUtil.createCityCode(coreCity.getPcode(),coreCity.getCode()));

        TbCoreCity city = queryByCode(coreCity.getCode());

        if(Fc.isBlank(coreCity.getId())){
            coreCity.setFullname(Fc.isNotBlank(coreCity.getFullname())?coreCity.getFullname():coreCity.getName());
            coreCity.setCountryCode(Fc.isNotBlank(coreCity.getCountryCode())?coreCity.getCountryCode():JpowerConstants.COUNTRY_CODE);
            coreCity.setPcode(Fc.isNotBlank(coreCity.getPcode())?coreCity.getPcode():JpowerConstants.TOP_CODE);

            JpowerAssert.notTrue(city != null, JpowerError.BUSINESS,"该编号已存在");
        }else{
            JpowerAssert.notTrue(city != null && !StringUtil.equals(coreCity.getId(),city.getId()), JpowerError.BUSINESS,"该编号已存在");
        }

        Boolean is = coreCityDao.saveOrUpdate(coreCity);
        return is;
    }

    @Override
    public TbCoreCity queryByCode(String cityCode) {
        return coreCityDao.getOne(Condition.<TbCoreCity>getQueryWrapper().lambda().eq(TbCoreCity::getCode,cityCode));
    }

    @Override
    public Boolean deleteBatch(List<String> ids) {

        List<Object> listCode = coreCityDao.listObjs(Condition.<TbCoreCity>getQueryWrapper().lambda().select(TbCoreCity::getCode).in(TbCoreCity::getId,ids));
        if(listCode.size()>0){
            Integer count = coreCityDao.count(Condition.<TbCoreCity>getQueryWrapper().lambda().in(TbCoreCity::getPcode,listCode));
            JpowerAssert.notTrue(count>0,JpowerError.BUSINESS,"请先删除子区域");
        }

        return coreCityDao.removeByIds(ids);
    }

}
