package com.qidiangk.smart.log.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.core.dbs.dbs.dao.JpowerServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogOperateMapper;
import com.qidiangk.smart.log.dbs.entity.LogOperate;

/**
 * @author mr.g
 */
@Repository
public class LogOperateDao extends JpowerServiceImpl<LogOperateMapper, LogOperate> {
}
