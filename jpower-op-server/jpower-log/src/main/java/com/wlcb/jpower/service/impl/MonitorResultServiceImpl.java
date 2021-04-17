package com.wlcb.jpower.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wlcb.jpower.dbs.dao.LogMonitorResultDao;
import com.wlcb.jpower.dbs.dao.mapper.LogMonitorResultMapper;
import com.wlcb.jpower.dbs.entity.TbLogMonitorResult;
import com.wlcb.jpower.module.common.page.PaginationContext;
import com.wlcb.jpower.module.common.service.impl.BaseServiceImpl;
import com.wlcb.jpower.module.mp.support.Condition;
import com.wlcb.jpower.service.MonitorResultService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 0:33
 */
@Service
@AllArgsConstructor
public class MonitorResultServiceImpl extends BaseServiceImpl<LogMonitorResultMapper,TbLogMonitorResult> implements MonitorResultService {

    private LogMonitorResultDao monitorResultDao;

    @Override
    public Page<TbLogMonitorResult> pageList(Map<String, Object> map) {
        return monitorResultDao.page(PaginationContext.getMpPage(), Condition.getQueryWrapper(map,TbLogMonitorResult.class).lambda().orderByDesc(TbLogMonitorResult::getCreateTime));
    }
}
