package com.wlcb.jpower.log.utils;

import java.util.Properties;

/**
 * @author ding
 * @description
 * @date 2020-11-19 11:31
 */
public class ElkPropsUtil {
    public ElkPropsUtil() {
    }

    public static String getDestination() {
        Properties props = System.getProperties();
        return props.getProperty("jpower.log.elk.destination", "");
    }
}
