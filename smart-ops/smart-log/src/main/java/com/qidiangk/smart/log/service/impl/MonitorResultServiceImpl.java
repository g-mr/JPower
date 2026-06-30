package com.qidiangk.smart.log.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.core.dbs.support.Wrappers;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.log.dbs.dao.LogMonitorResultDao;
import com.qidiangk.smart.log.dbs.dao.mapper.LogMonitorResultMapper;
import com.qidiangk.smart.log.dbs.entity.LogMonitorResult;
import com.qidiangk.smart.log.service.MonitorResultService;

import java.util.Map;

/**
 * @author mr.g
 */
@Service
@RequiredArgsConstructor
public class MonitorResultServiceImpl extends BaseServiceImpl<LogMonitorResultMapper, LogMonitorResult> implements MonitorResultService {

    private final LogMonitorResultDao monitorResultDao;

    @Override
    public Pg<LogMonitorResult> pageList(Map<String, Object> map) {
        return monitorResultDao.pg(Wrappers.getQueryWrapper(map).orderBy(LogMonitorResult::getCreateTime).desc());
    }
}
