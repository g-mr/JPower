package top.jpower.jpower.dbs.dao;

import top.jpower.jpower.dbs.dao.mapper.LogMonitorSettingMapper;
import top.jpower.jpower.dbs.entity.TbLogMonitorSetting;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogMonitorSettingDao extends JpowerServiceImpl<LogMonitorSettingMapper, TbLogMonitorSetting> {
}
