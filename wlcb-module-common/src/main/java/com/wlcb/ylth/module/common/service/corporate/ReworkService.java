package com.wlcb.ylth.module.common.service.corporate;

import com.wlcb.ylth.module.dbs.entity.base.PageBean;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgLog;
import com.wlcb.ylth.module.dbs.entity.corporate.TblCsrrgRework;

/**
 * @author mr.gmac
 */
public interface ReworkService {

    PageBean<TblCsrrgRework> listPage(TblCsrrgRework rework);

    TblCsrrgRework detail(String id);

    Integer updateReworkStatus(TblCsrrgLog log, String refuseReason);

    Integer addRework(TblCsrrgRework rework);

    Integer selectStatusCountByCid(String corporateId);
}
