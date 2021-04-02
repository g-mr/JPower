package com.wlcb.jpower.service;

import com.wlcb.jpower.properties.MonitorRestfulProperties;

/**
 * @author mr.g
 * @date 2021-04-02 11:25
 */
public interface TaskService {
    void process(MonitorRestfulProperties.Routes route);
}
