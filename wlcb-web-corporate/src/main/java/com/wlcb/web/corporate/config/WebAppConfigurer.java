package com.wlcb.web.corporate.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @ClassName WebAppConfigurer
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-01-31 14:56
 * @Version 1.0
 */
@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {

    @Value("${fileParentPath}")
    private String fileParentPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**/*.*")
                .addResourceLocations("file:"+fileParentPath);

        super.addResourceHandlers(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //拦截器不拦截的url
//        String [] exculudes = new String[]{"/icon/**","/bgImg/**","/login","/matter/days/queryDaysById"};
//        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(exculudes);
        super.addInterceptors(registry);
    }

}