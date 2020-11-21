package com.wlcb.jpower.log.utils;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;

/**
 * @author ding
 * @description
 * @date 2020-11-19 11:31
 */
public class ElkPropsUtil {

    public static String getDestination() {
        System.out.println(System.getenv("jpower.dev.elk.destination"));
        ConfigurableEnvironment environment = new StandardEnvironment();


        System.out.println(environment.getProperty("jpower.dev.elk.destination",String.class));
//        Properties props = System.getProperties();
//        return props.getProperty("jpower.log.elk.destination", "");
        return "";
    }
}
