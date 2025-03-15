package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.LogMonitorParamMapper;
import top.jpower.jpower.dbs.entity.TbLogMonitorParam;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogMonitorParamDao extends JpowerServiceImpl<LogMonitorParamMapper, TbLogMonitorParam> {
}
