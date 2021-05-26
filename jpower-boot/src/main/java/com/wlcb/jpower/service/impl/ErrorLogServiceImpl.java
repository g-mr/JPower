package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.dbs.dao.LogErrorDao;
import com.wlcb.jpower.dbs.dao.mapper.LogErrorMapper;
import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.service.ErrorLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class ErrorLogServiceImpl extends BaseServiceImpl<LogErrorMapper, TbLogError> implements ErrorLogService {

    private LogErrorDao errorDao;

    @Override
    public void saveErrorLog(ErrorLogDto errorLog) {
        TbLogError logError = BeanUtil.copyProperties(errorLog, TbLogError.class);
        errorDao.save(logError);
    }
}
