package com.wlcb.ylth.module.common.service.sync;

import com.wlcb.ylth.module.base.vo.ResponseData;
import com.wlcb.ylth.module.dbs.entity.sync.ImportInfo;

public interface SyncService {

    ResponseData sync(ImportInfo importInfo);

}
