package com.wlcb.jpower.module.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;

/**
 * @ClassName SpringUtil
 * @Description TODO spring 工具类
 * @Author 郭丁志
 * @Date 2020-07-24 17:12
 * @Version 1.0
 */
@Slf4j
public class SpringUtil implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        SpringUtil.context = context;
    }

    /**
     * @author 郭丁志
     * @Description //TODO 获取一个实例
     * @date 0:31 2020/8/12 0012
     * @param clazz
     * @return T
     */
    public static <T> T getBean(Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        return context.getBean(clazz);
    }

    public static <T> T getBean(String beanId) {
        if (beanId == null) {
            return null;
        }
        return (T) context.getBean(beanId);
    }

    public static boolean contains(String beanId) {
        return context.containsBean(beanId);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        if (null == beanName || "".equals(beanName.trim())) {
            return null;
        }
        if (clazz == null) {
            return null;
        }
        return (T) context.getBean(beanName, clazz);
    }

    public static ApplicationContext getContext() {
        if (context == null) {
            return null;
        }
        return context;
    }

    public static void publishEvent(ApplicationEvent event) {
        if (context == null) {
            return;
        }
        try {
            context.publishEvent(event);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

}
