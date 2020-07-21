package com.wlcb.jpower.module.common.service.core.dict.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wlcb.jpower.module.common.service.core.dict.CoreDictService;
import com.wlcb.jpower.module.dbs.dao.core.dict.TbCoreDictDao;
import com.wlcb.jpower.module.dbs.entity.core.dict.TbCoreDict;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mr.gmac
 */
@Service("coreDictService")
public class CoreDictServiceImpl implements CoreDictService {

    @Autowired
    private TbCoreDictDao paramsDao;

    @Override
    public TbCoreDict queryDictTypeByCode(String dictTypeCode, String code) {
        return paramsDao.getOne(new QueryWrapper<TbCoreDict>().lambda()
                .eq(TbCoreDict::getDictTypeCode,dictTypeCode)
                .eq(TbCoreDict::getCode,code)
                .eq(TbCoreDict::getStatus,1));
    }
}
