package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.LogMonitorSettingMapper;
import top.jpower.jpower.dbs.entity.TbLogMonitorSetting;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogMonitorSettingDao extends JpowerServiceImpl<LogMonitorSettingMapper, TbLogMonitorSetting> {
}
