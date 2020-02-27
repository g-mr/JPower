package com.wlcb.module.common.service.corporate.impl;

import com.wlcb.module.common.service.corporate.CorporateService;
import com.wlcb.module.dbs.dao.corporate.CorporateMapper;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgCorporate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("corporateService")
public class CorporateServiceImpl implements CorporateService {

    @Autowired
    private CorporateMapper corporateMapper;

    @Override
    public List<Map<String,String>> queryEnterpriseName(String name){
        return corporateMapper.selectName(name);
    }

    @Override
    public TblCsrrgCorporate selectById(String id) {
        return corporateMapper.selectById(id);
    }

}
