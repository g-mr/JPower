package top.jpower.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogOperateMapper;
import top.jpower.log.dbs.entity.LogOperate;

/**
 * @author mr.g
 */
@Repository
public class LogOperateDao extends JpowerServiceImpl<LogOperateMapper, LogOperate> {
}
