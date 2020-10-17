package com.wlcb.jpower.module.common.support;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @ClassName EnvBeanUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/10/16 0016 22:32
 * @Version 1.0
 */
@Component
public class EnvBeanUtil implements EnvironmentAware {

    private static Environment env;

    @Override
    public void setEnvironment(Environment environment) {
        env = environment;
    }

    public static String getString(String key) {
        return env.getProperty(key);
    }

    public static String getString(String key,String defaultValue) {
        return env.getProperty(key,defaultValue);
    }

    public static <T> T get(String key, Class<T> clz) {
        return env.getProperty(key,clz);
    }

    public static <T> T get(String key, Class<T> clz, T defaultValue) {
        return env.getProperty(key,clz,defaultValue);
    }
}
