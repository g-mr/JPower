package com.qidiangk.smart.log.service;

import com.alibaba.fastjson2.JSONArray;
import top.jpower.core.dbs.service.BaseService;
import com.qidiangk.smart.log.dbs.entity.LogMonitorParam;
import com.qidiangk.smart.log.dbs.entity.LogMonitorSetting;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 */
public interface MonitorSettingService extends BaseService<LogMonitorSetting> {

    /**
     * 保存设置
     * @author mr.g
     * @param setting
     * @return boolean
     */
    @Override
    boolean save(LogMonitorSetting setting);

    /**
     * 保存请求参数
     * @author mr.g
     * @param server
     * @param path
     * @param method
     * @param settingParams
     * @return java.lang.Boolean
     */
    Boolean saveParams(String server, String path, String method, List<LogMonitorParam> settingParams);

    /**
     * 删除不存在的配置
     * @author mr.g
     * @param server
     * @param restFulInfo
     * @param restFulTags
     * @return void
     */
    void deleteSetting(String server, Map<String, Object> restFulInfo, JSONArray restFulTags);

    /**
     * 获取一个地址配置
     * @author mr.g
     * @param name
     * @param tags
     * @param url
     * @param method
     * @return boolean
     */
    LogMonitorSetting getSetting(String name, List<String> tags, String url, String method);

    /**
     * 获取一个接口的配置
     * @Author mr.g
     * @param setting
     * @return top.jpower.jpower.dbs.entity.TbLogMonitorSetting
     **/
    LogMonitorSetting getOneSetting(LogMonitorSetting setting);

    /**
     * 查询一个监控地址的所有参数
     * @Author mr.g
     * @param server
     * @param url
     * @return java.util.List<top.jpower.jpower.dbs.entity.TbLogMonitorSettingParam>
     **/
    List<LogMonitorParam> queryParamByPath(String server, String url);
}
