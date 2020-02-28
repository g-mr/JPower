package com.wlcb.wlj.module.common.service.corporate;

import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporate;

import java.util.List;
import java.util.Map;

public interface CorporateService {

    List<Map<String,String>> queryEnterpriseName(String name);

    TblCsrrgCorporate selectById(String id);
}
