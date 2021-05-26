package com.wlcb.jpower.dbs.dao;

import com.wlcb.jpower.dbs.dao.mapper.LogOperateMapper;
import com.wlcb.jpower.dbs.entity.TbLogOperate;
import com.wlcb.jpower.module.dbs.dao.JpowerServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogOperateDao extends JpowerServiceImpl<LogOperateMapper, TbLogOperate> {
}
