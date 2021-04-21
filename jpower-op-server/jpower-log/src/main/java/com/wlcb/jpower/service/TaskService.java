package com.wlcb.jpower.service;

import com.alibaba.fastjson.JSONArray;
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

    /**
     * 获取一个监控得树形列表
     * @author mr.g
     * @return com.alibaba.fastjson.JSONArray
     * @param route
     */
    JSONArray tree(MonitorRestfulProperties.Route route);

    /**
     * 获取一个接口下的所有参数
     * @author mr.g
     * @param route
     * @param path
     * @return java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
     */
    List<Map<String, Object>> getParams(MonitorRestfulProperties.Route route, String tag, String path);
}
