package com.wlcb.jpower.module.configurer.client;

import com.wlcb.jpower.module.common.utils.CollectionUtil;
import com.wlcb.jpower.module.properties.AuthDefExculdesUrl;
import com.wlcb.jpower.module.properties.AuthProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @ClassName AuthConfigurer
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 22:19
 * @Version 1.0
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties({AuthProperties.class})
public class ClientConfigurer implements WebMvcConfigurer {

    private AuthProperties authProperties;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        authProperties.getClient().forEach(client -> {
            InterceptorRegistration registration = registry.addInterceptor(new ClientInterceptor(client.getCode()));
            if (CollectionUtil.isNotEmpty(client.getPath())){
                registration.addPathPatterns(client.getPath());
            }else {
                registration.addPathPatterns("/**");
            }

            client.getExcludePath().addAll(AuthDefExculdesUrl.getExculudesUrl());
            registration.excludePathPatterns(client.getExcludePath());
        });
    }

}
