package top.jpower.log.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogOperateMapper;
import top.jpower.log.dbs.entity.LogOperate;
import top.jpower.log.service.OperateLogService;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class OperateLogServiceImpl extends BaseServiceImpl<LogOperateMapper, LogOperate> implements OperateLogService {

}
