package com.wlcb.wlj.module.common.Interceptor;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;

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
        //拦截器不拦截的url
        String [] exculudes = new String[]{"/login"};
        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(exculudes);
        super.addInterceptors(registry);
    }

}