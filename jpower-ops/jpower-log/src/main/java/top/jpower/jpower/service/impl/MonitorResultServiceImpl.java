package top.jpower.jpower.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import top.jpower.jpower.dbs.dao.LogMonitorResultDao;
import top.jpower.jpower.dbs.dao.mapper.LogMonitorResultMapper;
import top.jpower.jpower.dbs.entity.TbLogMonitorResult;
import top.jpower.jpower.module.page.PaginationContext;
import top.jpower.jpower.module.service.impl.BaseServiceImpl;
import top.jpower.jpower.module.mp.support.Condition;
import top.jpower.jpower.service.MonitorResultService;

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
