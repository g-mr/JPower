package com.wlcb.jpower.module.common.deploy.service;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

/**
 * 启动前置处理器
 *
 * @author mr.g
 **/
public interface DeployService extends Ordered {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplicationBuilder
     * @param appName AppName
     * @param profile Profile
     */
    void deploy(SpringApplicationBuilder builder, String appName, String profile);

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
