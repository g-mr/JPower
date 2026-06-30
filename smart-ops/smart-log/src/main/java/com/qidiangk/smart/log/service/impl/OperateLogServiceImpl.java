package com.qidiangk.smart.log.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogOperateMapper;
import com.qidiangk.smart.log.dbs.entity.LogOperate;
import com.qidiangk.smart.log.service.OperateLogService;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class OperateLogServiceImpl extends BaseServiceImpl<LogOperateMapper, LogOperate> implements OperateLogService {

}
