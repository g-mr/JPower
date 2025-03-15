package top.jpower.jpower.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import top.jpower.jpower.dbs.entity.TbLogMonitorResult;
import top.jpower.core.dbs.service.BaseService;

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
     * @return com.baomidou.mybatisplus.extension.plugins.pagination.Page<top.jpower.jpower.dbs.entity.TbLogMonitorResult>
     */
    Page<TbLogMonitorResult> pageList(Map<String,Object> map);
}
