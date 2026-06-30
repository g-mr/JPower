package com.qidiangk.smart.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogErrorMapper;
import com.qidiangk.smart.log.dbs.entity.LogError;

/**
 * @author mr.g
 */
@Repository
public class LogErrorDao extends JpowerServiceImpl<LogErrorMapper, LogError> {
}
