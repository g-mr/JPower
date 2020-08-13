package com.wlcb.jpower.module.common.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName WebAppConfigurer
 * @Description TODO 不用走鉴权的接口去除
 * @Author 郭丁志
 * @Date 2020-01-31 14:56
 * @Version 1.0
 */
@Configuration
public class LoginConfigurer implements WebMvcConfigurer {

    /** 所有实现了AuthExculdesUrl接口的实现类 **/
    private final List<AuthExculdesUrl> delegates = new ArrayList<>();

    @Resource
    private LoginInterceptor loginInterceptor;

    /**
     * @author 郭丁志
     * @Description TODO 把所有实现了AuthExculdesUrl接口的实现类注入进来
     */
    @Autowired(required = false)
    public void setConfigurers(List<AuthExculdesUrl> interceptorExculdesUrls) {
        if (!CollectionUtils.isEmpty(interceptorExculdesUrls)) {
            delegates.addAll(interceptorExculdesUrls);
        }
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        List<String> exculudesUrl = new ArrayList<>();

        for (AuthExculdesUrl delegate : this.delegates) {
            delegate.addExculdesUrl(exculudesUrl);
        }
        //拦截器不拦截的url  默认auth接口统一不做拦截
        exculudesUrl.add("/auth/**");
        exculudesUrl.add("/v2/api-docs-ext/**");
        exculudesUrl.add("/v2/api-docs/**");
        exculudesUrl.add("/doc.html");
        exculudesUrl.add("/swagger-resources");

        registry.addInterceptor(loginInterceptor).addPathPatterns("/**").excludePathPatterns(exculudesUrl);
    }

}