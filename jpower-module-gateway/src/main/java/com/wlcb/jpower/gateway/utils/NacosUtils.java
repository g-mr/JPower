package com.wlcb.jpower.gateway.utils;

import java.util.Properties;

/**
 * @ClassName NacosUtils
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-08-26 10:45
 * @Version 1.0
 */
public class NacosUtils {

    public static String getRouteDataId() {
        Properties props = System.getProperties();
        return new StringBuffer(props.getProperty("spring.application.name")).append("-").append(props.getProperty("spring.profiles.active")).append(".json").toString();
    }

}
