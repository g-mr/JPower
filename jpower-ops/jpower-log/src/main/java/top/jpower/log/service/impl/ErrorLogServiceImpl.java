package top.jpower.log.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.core.dbs.service.impl.BaseServiceImpl;
import top.jpower.log.dbs.dao.mapper.LogErrorMapper;
import top.jpower.log.dbs.entity.LogError;
import top.jpower.log.service.ErrorLogService;

/**
 * @author mr.g
 */
@Service
@AllArgsConstructor
public class ErrorLogServiceImpl extends BaseServiceImpl<LogErrorMapper, LogError> implements ErrorLogService {

}
