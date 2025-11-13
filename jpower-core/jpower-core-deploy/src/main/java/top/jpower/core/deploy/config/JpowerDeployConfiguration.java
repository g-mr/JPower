package top.jpower.core.deploy.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import top.jpower.core.deploy.property.JpowerProperties;

/**
 * 配置类
 *
 * @author mr.g
 */
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JpowerProperties.class)
public class JpowerDeployConfiguration {
}
