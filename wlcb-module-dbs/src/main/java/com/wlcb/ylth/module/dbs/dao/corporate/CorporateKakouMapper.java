package com.wlcb.ylth.module.dbs.dao.corporate;

import com.wlcb.ylth.module.dbs.dao.BaseMapper;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgCorporateKakou;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("corporateKakouMapper")
public interface CorporateKakouMapper extends BaseMapper<TblCsrrgCorporateKakou> {

    List<Map<String, String>> listKakou(TblCsrrgCorporateKakou kakou);

    Integer selectKakouById(String id);

    TblCsrrgCorporateKakou selectKakouByCorporateId(String corporateId);
}
