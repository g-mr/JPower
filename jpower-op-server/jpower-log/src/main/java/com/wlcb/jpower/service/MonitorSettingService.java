package com.wlcb.jpower.service;

import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSettingParam;
import com.wlcb.jpower.module.common.service.BaseService;

import java.util.List;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 19:15
 */
public interface MonitorSettingService extends BaseService<TbLogMonitorSetting> {

    /**
     * 保存设置
     * @author mr.g
     * @param setting
     * @return boolean
     */
    @Override
    boolean save(TbLogMonitorSetting setting);

    /**
     * 查询监控设置
     * @author mr.g
     * @param server 服务名称
     * @param tag 所属分组
     * @param path 监控路径
     * @param method 请求方式
     * @return com.wlcb.jpower.dbs.entity.TbLogMonitorSetting
     */
    TbLogMonitorSetting setting(String server, String tag, String path, String method);

    /**
     * 查询设置的全部参数
     * @author mr.g
     * @param settingId
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> queryParam(String settingId);

    /**
     * 保存请求参数
     * @author mr.g
     * @param settingId
     * @param settingParams
     * @return java.lang.Boolean
     */
    Boolean saveParams(String settingId, List<TbLogMonitorSettingParam> settingParams);
}
