package com.wlcb.jpower.service.impl;

import com.wlcb.jpower.dbs.dao.mapper.LogErrorMapper;
import com.wlcb.jpower.dbs.entity.TbLogError;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.service.ErrorLogService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
@Service
@AllArgsConstructor
public class ErrorLogServiceImpl extends BaseServiceImpl<LogErrorMapper, TbLogError> implements ErrorLogService {

}
