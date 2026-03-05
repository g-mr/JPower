package top.jpower.jpower.gateway.utils;

import java.util.Properties;

/**
 * Nacos 工具类
 *
 * @author mr.g
 */
public class NacosUtils {

    public static String getRouteDataId() {
        Properties props = System.getProperties();
        return new StringBuffer(props.getProperty("spring.application.name")).append("-").append(props.getProperty("spring.profiles.active")).append(".json").toString();
    }

}
