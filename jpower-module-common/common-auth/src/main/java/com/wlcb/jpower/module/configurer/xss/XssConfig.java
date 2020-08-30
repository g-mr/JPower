package com.wlcb.jpower.module.configurer.xss;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName XssConfig
 * @Description TODO XSS配置器
 * @Author 郭丁志
 * @Date 2020-05-01 23:22
 * @Version 1.0
 */
@Configuration
public class XssConfig {
    @Bean
    public FilterRegistrationBean xssFilterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new XssFilter());
        filterRegistrationBean.setOrder(1);
        filterRegistrationBean.setEnabled(true);
        filterRegistrationBean.addUrlPatterns("/*");
        Map<String, String> initParameters = new HashMap<>();
        initParameters.put("excludes", "/favicon.ico,/img/*,/js/*,/css/*");
        //是否开启富文本过滤，目前已经开启，需要的时候关闭即可
        initParameters.put("isIncludeRichText", "false");
        filterRegistrationBean.setInitParameters(initParameters);
        return filterRegistrationBean;
    }

}

