package com.wlcb.wlj.module.common.service.corporate;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRecord;

public interface RecordService {

    PageBean<TblCsrrgRecord> listPage(TblCsrrgRecord record);

    ResponseData addRecord(TblCsrrgRecord record);

    TblCsrrgRecord detail(String id);

    Integer updateRecordStatus(TblCsrrgLog recordLog, String failReason);

    Integer selectCountByCidAndOid(String openid, String corporateId);
}
