package com.wlcb.ylth.module.common.service.corporate;

import com.github.pagehelper.PageInfo;
import com.wlcb.ylth.module.base.vo.ResponseData;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgRecord;

public interface RecordService {

    PageInfo<TblCsrrgRecord> listPage(TblCsrrgRecord record);

    ResponseData addRecord(TblCsrrgRecord record);

    TblCsrrgRecord detail(String id);

    Integer updateRecordStatus(TblCsrrgLog recordLog, String failReason);

    Integer selectCountByCidAndOid(String openid, String corporateId);

}
