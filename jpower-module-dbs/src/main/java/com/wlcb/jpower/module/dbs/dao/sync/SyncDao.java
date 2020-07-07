package com.wlcb.jpower.module.dbs.dao.sync;

import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import com.wlcb.jpower.module.dbs.dao.sync.mapper.SyncMapper;
import com.wlcb.jpower.module.dbs.entity.sync.ImportInfo;
import org.springframework.stereotype.Repository;

/**
 * @ClassName SyncDao
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-03 15:19
 * @Version 1.0
 */
@Repository
public class SyncDao extends JpowerServiceImpl<SyncMapper, ImportInfo> {
}
