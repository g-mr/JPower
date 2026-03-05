package top.jpower.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogMonitorResultMapper;
import top.jpower.log.dbs.entity.LogMonitorResult;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorResultDao extends JpowerServiceImpl<LogMonitorResultMapper, LogMonitorResult> {
}
