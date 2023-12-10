package top.jpower.jpower.dbs.dao;

import top.jpower.jpower.dbs.dao.mapper.LogErrorMapper;
import top.jpower.jpower.dbs.entity.TbLogError;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogErrorDao extends JpowerServiceImpl<LogErrorMapper, TbLogError> {
}
