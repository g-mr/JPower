package com.wlcb.jpower.log.deploy;

import com.wlcb.jpower.module.common.deploy.service.DeployService;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * @ClassName DeployService
 * @Description TODO 启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
public class LogDeployServiceImpl implements DeployService {

    @Override
    public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
        Properties props = System.getProperties();
        props.setProperty("logging.config", "classpath:logback-spring.xml");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
