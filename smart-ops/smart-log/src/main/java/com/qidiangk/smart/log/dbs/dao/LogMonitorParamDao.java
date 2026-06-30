package com.qidiangk.smart.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogMonitorParamMapper;
import com.qidiangk.smart.log.dbs.entity.LogMonitorParam;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorParamDao extends JpowerServiceImpl<LogMonitorParamMapper, LogMonitorParam> {
}
