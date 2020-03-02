package com.wlcb.wlj.module.common.service.corporate;

import com.wlcb.wlj.module.dbs.entity.base.PageBean;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.wlj.module.dbs.entity.corporate.TblCsrrgRework;

/**
 * @author mr.gmac
 */
public interface ReworkService {

    PageBean<TblCsrrgRework> listPage(TblCsrrgRework rework);

    TblCsrrgRework detail(String id);

    Integer updateReworkStatus(TblCsrrgLog log, String refuseReason);

    Integer addRework(TblCsrrgRework rework);
}
