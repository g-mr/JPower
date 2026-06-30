package com.qidiangk.smart.gateway.gateway.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;

import java.util.Collections;

/**
 * @author mr.g
 * @date 2024/2/26 9:56 AM
 */
@AutoService(DeployService.class)
public class GateWayDeployServiceImpl implements DeployService {
    /**
     * 启动时 处理 SpringApplicationBuilder
     *
     * @param builder    SpringApplicationBuilder 启动器
     * @param properties 配置
     * @param appName    项目名称
     * @param profile    环境变量
     */
    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {
		properties.getPropertySources().addFirst(new MapPropertySource("gateWayDeployService", Collections.singletonMap("csp.sentinel.app.type", "1")));
    }
}
