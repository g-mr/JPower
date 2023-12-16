package top.jpower.jpower.dbs.dao;

import org.springframework.stereotype.Repository;
import top.jpower.jpower.dbs.dao.mapper.LogOperateMapper;
import top.jpower.jpower.dbs.entity.TbLogOperate;
import top.jpower.jpower.module.dbs.dao.JpowerServiceImpl;

/**
 * @author mr.g
 * @date 2021-04-07 16:10
 */
@Repository
public class LogOperateDao extends JpowerServiceImpl<LogOperateMapper, TbLogOperate> {
}
