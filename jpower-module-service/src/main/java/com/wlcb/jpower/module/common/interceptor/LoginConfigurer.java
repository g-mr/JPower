package com.wlcb.jpower.module.common.interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WebAppConfigurer
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-31 14:56
 * @Version 1.0
 */
@Configuration
public class LoginConfigurer extends WebMvcConfigurerAdapter {


    @Resource
    private LoginInterceptor loginInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exculudesUrl = new ArrayList<>();

        //拦截器不拦截的url  默认login接口统一不做拦截
        exculudesUrl.add("/auth/**");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(exculudesUrl);
        super.addInterceptors(registry);
    }

}