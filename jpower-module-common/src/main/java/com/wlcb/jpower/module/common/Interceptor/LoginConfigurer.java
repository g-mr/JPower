package com.wlcb.jpower.module.common.Interceptor;

import com.wlcb.jpower.module.common.utils.param.ParamConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
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

    private final String noLoginUrl = "noLoginUrl";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exculudesUrl = new ArrayList<>();

        String urls = ParamConfig.getString(noLoginUrl);
        if (StringUtils.isNotBlank(urls)){
            exculudesUrl = new ArrayList<>(Arrays.asList(urls.split(",")));
        }

        //拦截器不拦截的url  默认login接口统一不做拦截
        exculudesUrl.add("/login");
        exculudesUrl.add("/wxlogin");
        exculudesUrl.add("/loginVercode");
        exculudesUrl.add("/phoneLogin");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(exculudesUrl);
        super.addInterceptors(registry);
    }

}