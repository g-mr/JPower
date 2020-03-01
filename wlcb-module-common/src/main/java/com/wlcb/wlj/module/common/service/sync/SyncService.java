package com.wlcb.wlj.module.common.service.sync;

import com.wlcb.wlj.module.base.vo.ResponseData;
import com.wlcb.wlj.module.dbs.entity.sync.ImportInfo;

public interface SyncService {

    ResponseData sync(ImportInfo importInfo);

}
