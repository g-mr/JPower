package top.jpower.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogMonitorSettingMapper;
import top.jpower.log.dbs.entity.LogMonitorSetting;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorSettingDao extends JpowerServiceImpl<LogMonitorSettingMapper, LogMonitorSetting> {
}
