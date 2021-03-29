package com.wlcb.jpower.module.common.deploy.config;

import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 配置类
 */
@Configuration(proxyBeanMethods = false)
@AllArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({
	JpowerProperties.class
})
public class JpowerDeployConfiguration {

}
