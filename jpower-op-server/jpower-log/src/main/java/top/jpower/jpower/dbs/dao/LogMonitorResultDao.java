package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.LogMonitorResultMapper;
import top.jpower.jpower.dbs.entity.TbLogMonitorResult;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogMonitorResultDao extends JpowerServiceImpl<LogMonitorResultMapper, TbLogMonitorResult> {
}
