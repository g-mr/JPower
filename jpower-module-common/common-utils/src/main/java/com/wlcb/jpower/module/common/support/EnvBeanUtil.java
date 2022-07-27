package com.wlcb.jpower.module.common.support;

import com.wlcb.jpower.module.common.utils.Fc;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * 环境变量获取
 *
 * @author mr.g
 **/
@Component("envBeanUtil")
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

    public static String getProfile() {
        if (Fc.notNull(env)){
            return env.getProperty("jpower.env",String.class);
        }
        return null;
    }

    public static Boolean getTenantEnable() {
        if (Fc.notNull(env)){
            return env.getProperty("jpower.tenant.enable",Boolean.class);
        }
        return Boolean.FALSE;
    }

    public static Boolean getDemoEnable() {
        if (Fc.notNull(env)){
            return env.getProperty("jpower.demo.enable",Boolean.class);
        }
        return Boolean.FALSE;
    }
}
