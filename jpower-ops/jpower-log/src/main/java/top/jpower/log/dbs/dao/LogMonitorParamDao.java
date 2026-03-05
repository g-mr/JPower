package top.jpower.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogMonitorParamMapper;
import top.jpower.log.dbs.entity.LogMonitorParam;

/**
 * @author mr.g
 */
@Repository
public class LogMonitorParamDao extends JpowerServiceImpl<LogMonitorParamMapper, LogMonitorParam> {
}
