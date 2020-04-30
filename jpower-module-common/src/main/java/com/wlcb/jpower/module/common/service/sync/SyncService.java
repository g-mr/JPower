package com.wlcb.jpower.module.common.service.sync;

import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.dbs.entity.sync.ImportInfo;

public interface SyncService {

    ResponseData sync(ImportInfo importInfo);

}
