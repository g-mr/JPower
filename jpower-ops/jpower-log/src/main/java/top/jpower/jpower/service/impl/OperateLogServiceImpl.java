package top.jpower.jpower.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.mapper.LogOperateMapper;
import top.jpower.jpower.dbs.entity.TbLogOperate;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.jpower.service.OperateLogService;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class OperateLogServiceImpl extends BaseServiceImpl<LogOperateMapper, TbLogOperate> implements OperateLogService {

}
