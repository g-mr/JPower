package com.wlcb.jpower.module.common.utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.core.Ordered;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * Spring工具类
 *
 * @author mr.g
 */
@Slf4j
@Component
public class SpringUtil implements ApplicationContextAware, Ordered {

    /**
     * ApplicationContext
     **/
    @Getter
    private static ApplicationContext context;

    /**
     * 获取ApplicationContext
     *
     * @author mr.g
     * @param context ApplicationContext
     **/
    @Override
    public void setApplicationContext(@NonNull ApplicationContext context) throws BeansException {
        SpringUtil.context = context;
    }

    /**
     * 获取bean
     *
     * @author mr.g
     * @param clazz class
     * @return 实例
     **/
    public static <T> T getBean(Class<T> clazz) {
        return Fc.isNull(clazz) ? null : context.getBean(clazz);
    }

    /**
     * 获取bean
     *
     * @author mr.g
     * @param beanId bean名称
     * @return 实例
     **/
    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanId) {
        return Fc.isBlank(beanId) ? null : (T) context.getBean(beanId);
    }

    /**
     * 是否存在bean
     *
     * @author mr.g
     * @param beanId bean名称
     * @return 是否存在
     **/
    public static boolean contains(String beanId) {
        return context.containsBean(beanId);
    }

    /**
     * 发布一个spring事件
     *
     * @author mr.g
     * @param event 事件
     **/
    public static void publishEvent(ApplicationEvent event) {
        if (Fc.notNull(context)){
            context.publishEvent(event);
        }
    }

    /**
     * 排序
     *
     * @author mr.g
     * @return 顺序
     **/
    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}
