package com.wlcb.module.common.service.corporate;

import com.wlcb.module.base.vo.ResponseData;
import com.wlcb.module.dbs.entity.base.PageBean;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRecord;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRecordLog;

public interface RecordService {

    PageBean<TblCsrrgRecord> listPage(TblCsrrgRecord record);

    ResponseData addRecord(TblCsrrgRecord record);

    TblCsrrgRecord detail(String id);

    Integer updateRecordStatus(TblCsrrgRecordLog recordLog,String failReason);
}
