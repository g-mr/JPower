package com.qidiangk.smart.system.api.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

/**
 * resource-api 自动配置
 * <p>
 * 确保 feign 包下的 Fallback 组件（@Component）能被依赖方自动扫描注册
 * </p>
 *
 * @author mr.g
 */
@AutoConfiguration
@ComponentScan({"com.qidiangk.smart.system.api.feign", "com.qidiangk.smart.system.api.wrapper"})
public class SystemApiAutoConfiguration {
}
