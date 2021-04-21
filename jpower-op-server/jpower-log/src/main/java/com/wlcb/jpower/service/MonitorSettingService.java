package com.wlcb.jpower.service;

import com.alibaba.fastjson.JSONArray;
import com.wlcb.jpower.dbs.entity.TbLogMonitorParam;
import com.wlcb.jpower.dbs.entity.TbLogMonitorSetting;
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
     * 保存请求参数
     * @author mr.g
     * @param server
     * @param path
     * @param method
     * @param settingParams
     * @return java.lang.Boolean
     */
    Boolean saveParams(String server, String path, String method, List<TbLogMonitorParam> settingParams);

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
    TbLogMonitorSetting getSetting(String name, List<String> tags, String url, String method);

    /**
     * 获取一个接口的配置
     * @Author mr.g
     * @param setting
     * @return com.wlcb.jpower.dbs.entity.TbLogMonitorSetting
     **/
    TbLogMonitorSetting getOneSetting(TbLogMonitorSetting setting);

    /**
     * 查询一个监控地址的所有参数
     * @Author mr.g
     * @param server
     * @param url
     * @return java.util.List<com.wlcb.jpower.dbs.entity.TbLogMonitorSettingParam>
     **/
    List<TbLogMonitorParam> queryParamByPath(String server, String url);
}
