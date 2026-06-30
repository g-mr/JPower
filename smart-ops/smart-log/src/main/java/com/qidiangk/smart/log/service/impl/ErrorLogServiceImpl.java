package com.qidiangk.smart.log.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import com.qidiangk.smart.log.dbs.dao.mapper.LogErrorMapper;
import com.qidiangk.smart.log.dbs.entity.LogError;
import com.qidiangk.smart.log.service.ErrorLogService;

/**
 * @author mr.g
 */
@Service
@AllArgsConstructor
public class ErrorLogServiceImpl extends BaseServiceImpl<LogErrorMapper, LogError> implements ErrorLogService {

}
