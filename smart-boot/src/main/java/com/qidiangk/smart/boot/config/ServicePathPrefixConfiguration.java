package com.qidiangk.smart.boot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerTypePredicate;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import com.qidiangk.smart.common.constants.AppConstant;

/**
 * 单体应用服务路径前缀配置
 * <p>
 * 为不同子服务的 Controller 自动添加服务名称作为接口路径前缀，
 * 使来自不同服务的接口在 jpower-boot 中具有明确的、可区分的访问路径。
 * <p>
 *
 * @author mr.g
 */
@Configuration(proxyBeanMethods = false)
public class ServicePathPrefixConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        AppConstant.SERVICE_PREFIX_MAP.forEach((basePackage, prefix) ->
                configurer.addPathPrefix(prefix, HandlerTypePredicate.forBasePackage(basePackage))
        );
    }

}
