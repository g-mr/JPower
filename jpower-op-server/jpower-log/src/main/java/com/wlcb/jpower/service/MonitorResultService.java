package com.wlcb.jpower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 0:33
 */
public interface MonitorResultService extends BaseService<TbLogMonitorResult> {

    /**
     * 查询监控结果列表
     * @author mr.g
     * @param map
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<com.wlcb.jpower.dbs.entity.TbLogMonitorResult>
     */
    Page<TbLogMonitorResult> pageList(Map<String,Object> map);
}
