package com.wlcb.module.common.service.corporate;

import com.wlcb.module.dbs.entity.base.PageBean;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.module.dbs.entity.corporate.TblCsrrgRework;

/**
 * @author mr.gmac
 */
public interface ReworkService {

    PageBean<TblCsrrgRework> listPage(String corporateId);

    TblCsrrgRework detail(String id);

    Integer updateReworkStatus(TblCsrrgLog log, String refuseReason);

    Integer addRework(TblCsrrgRework rework);
}
