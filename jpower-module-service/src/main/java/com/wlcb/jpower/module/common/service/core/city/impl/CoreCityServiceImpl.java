package com.wlcb.jpower.module.common.service.core.city.impl;

import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.dbs.dao.core.city.TbCoreCityDao;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
public class CoreCityServiceImpl implements CoreCityService {

    @Autowired
    private TbCoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listCodeNme(TbCoreCity coreCity) {

        return coreCityDao.listMaps(Condition.getQueryWrapper(coreCity).lambda()
                .select(TbCoreCity::getCode,TbCoreCity::getName)
                .orderByAsc(TbCoreCity::getSortNum));
    }

    @Override
    public List<TbCoreCity> listChild(Map<String, Object> coreCity) {
        return coreCityDao.list(Condition.getQueryWrapper(coreCity,TbCoreCity.class).lambda().orderByAsc(TbCoreCity::getSortNum));
    }

}
