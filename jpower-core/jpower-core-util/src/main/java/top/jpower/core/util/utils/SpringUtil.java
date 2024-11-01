package top.jpower.core.util.utils;

import lombok.Getter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * Spring工具类
 *
 * @author mr.g
 */
@Component
public class SpringUtil extends cn.hutool.extra.spring.SpringUtil implements ApplicationListener<ApplicationStartingEvent> {

    @Getter
    private static SpringApplication springApplication;

    /**
     * 是否存在bean
     *
     * @author mr.g
     * @param beanId bean名称
     * @return 是否存在
     **/
    public static boolean contains(String beanId) {
        return getBeanFactory().containsBean(beanId);
    }

    /**
     * 获取主类
     *
     * @author mr.g
     * @return 主类
     **/
    public static Class<?> getMainClass(){
        return springApplication.getMainApplicationClass();

    }

    @Override
    public void onApplicationEvent(ApplicationStartingEvent event) {
        springApplication = event.getSpringApplication();
    }

}
