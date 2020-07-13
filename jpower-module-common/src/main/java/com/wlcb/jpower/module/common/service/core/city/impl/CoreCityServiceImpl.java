package com.wlcb.jpower.module.common.service.core.city.impl;

import com.wlcb.jpower.module.common.service.core.city.CoreCityService;
import com.wlcb.jpower.module.dbs.dao.core.city.TbCoreCityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreCityService")
public class CoreCityServiceImpl implements CoreCityService {

    @Autowired
    private TbCoreCityDao coreCityDao;

}
