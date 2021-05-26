package com.wlcb.jpower.service;

import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.common.service.BaseService;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
public interface ErrorLogService extends BaseService<TbLogError> {

    void saveErrorLog(ErrorLogDto errorLog);
}
