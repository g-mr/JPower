package com.wlcb.module.dbs.dao.corporate;

import com.wlcb.module.dbs.dao.BaseMapper;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRecord;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mr.gmac
 */
@Component("recordMapper")
public interface RecordMapper extends BaseMapper<TblCsrrgRecord> {

    List<TblCsrrgRecord> listAll(TblCsrrgRecord record);

    Integer selectCountByCidAndOid(String openid, String corporateId);
}
