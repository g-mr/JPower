package com.wlcb.jpower.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.properties.MonitorRestfulProperties;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2021-04-02 11:25
 */
public interface TaskService {

    /**
     * 接口监控实现
     * @Author mr.g
     * @param route
     * @return void
     **/
    void process(MonitorRestfulProperties.Route route);

    /**
     * 获取服务分组列表
     * @Author mr.g
     * @param name
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     **/
    JSONArray tagList(MonitorRestfulProperties.Route name);
}
