package top.jpower.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogErrorMapper;
import top.jpower.log.dbs.entity.LogError;

/**
 * @author mr.g
 */
@Repository
public class LogErrorDao extends JpowerServiceImpl<LogErrorMapper, LogError> {
}
