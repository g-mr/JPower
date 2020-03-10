package com.wlcb.wlj.module.dbs.dao.corporate;

import com.wlcb.wlj.module.dbs.dao.BaseMapper;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgCorporateKakou;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component("corporateKakouMapper")
public interface CorporateKakouMapper extends BaseMapper<TblCsrrgCorporateKakou> {

    List<Map<String, String>> listKakou(TblCsrrgCorporateKakou kakou);

    Integer selectKakouById(String id);

}
