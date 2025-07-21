package top.jpower.core.log.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.jpower.core.log.property.JpowerLogProperties;

/**
 * 启用JpowerLogProperties配置
 *
 * @author mr.g
 * @date 2025-7-21 22:57
 */
@AutoConfiguration
@EnableConfigurationProperties(JpowerLogProperties.class)
public class LogConfig {
}
