package com.wlcb.jpower.module.common.service.core.city.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.dbs.dao.core.city.TbCoreCityDao;
import com.wlcb.jpower.module.dbs.entity.core.city.TbCoreCity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
public class CoreCityServiceImpl implements CoreCityService {

    @Autowired
    private TbCoreCityDao coreCityDao;

    @Override
    public List<Map<String, Object>> listChild(String code) {
        return coreCityDao.listMaps(new QueryWrapper<TbCoreCity>().lambda()
                .select(TbCoreCity::getCode,TbCoreCity::getName)
                .eq(TbCoreCity::getPcode,code)
                .eq(TbCoreCity::getStatus,1)
                .orderByAsc(TbCoreCity::getSortNum));
    }
}
