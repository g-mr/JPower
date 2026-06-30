package com.qidiangk.smart.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogMonitorSettingMapper;
import com.qidiangk.smart.log.dbs.entity.LogMonitorSetting;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorSettingDao extends JpowerServiceImpl<LogMonitorSettingMapper, LogMonitorSetting> {
}
