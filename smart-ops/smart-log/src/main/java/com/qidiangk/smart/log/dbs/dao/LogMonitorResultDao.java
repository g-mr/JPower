package com.qidiangk.smart.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogMonitorResultMapper;
import com.qidiangk.smart.log.dbs.entity.LogMonitorResult;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorResultDao extends JpowerServiceImpl<LogMonitorResultMapper, LogMonitorResult> {
}
