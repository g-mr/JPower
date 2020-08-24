package com.wlcb.jpower.module.common.deploy.service;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

/**
 * @ClassName DeployService
 * @Description TODO 启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
public interface DeployService extends Ordered, Comparable<DeployService> {

    /**
     * 启动时 处理 SpringApplicationBuilder
     * @param builder SpringApplicationBuilder
     * @param appName SpringApplicationAppName
     * @param profile SpringApplicationProfile
     */
    void launcher(SpringApplicationBuilder builder, String appName, String profile);

    /**
     * 获取排列顺序
     * @return order
     */
    @Override
    default int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 对比排序
     * @param o LauncherService
     * @return compare
     */
    @Override
    default int compareTo(DeployService o) {
        return Integer.compare(this.getOrder(), o.getOrder());
    }

}
