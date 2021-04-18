package com.wlcb.jpower.service;

import com.alibaba.fastjson.JSONArray;
import com.wlcb.jpower.properties.MonitorRestfulProperties;

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

    /**
     * 获取一个监控得树形列表
     * @author mr.g
     * @return com.alibaba.fastjson.JSONArray
     * @param route
     */
    JSONArray tree(MonitorRestfulProperties.Route route);
}
