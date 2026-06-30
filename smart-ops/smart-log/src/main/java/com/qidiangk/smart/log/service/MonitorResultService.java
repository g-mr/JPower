package com.qidiangk.smart.log.service;

import top.jpower.core.dbs.service.BaseService;
import top.jpower.core.util.rsp.Pg;
import com.qidiangk.smart.log.dbs.entity.LogMonitorResult;

import java.util.Map;

/**
 * @author mr.g
 */
public interface MonitorResultService extends BaseService<LogMonitorResult> {

    /**
     * 查询监控结果列表
     * @author mr.g
     * @param map 查询参数
     * @return 分页列表
     */
	Pg<LogMonitorResult> pageList(Map<String,Object> map);
}
