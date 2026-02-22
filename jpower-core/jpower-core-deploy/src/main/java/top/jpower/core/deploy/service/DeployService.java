package top.jpower.core.deploy.service;

import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 启动前置处理器
 *
 * @author mr.g
 **/
public interface DeployService extends Ordered {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplicationBuilder 启动器
     * @param properties 配置
     * @param appName 项目名称
     * @param profile 环境变量
     */
    void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile);

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
