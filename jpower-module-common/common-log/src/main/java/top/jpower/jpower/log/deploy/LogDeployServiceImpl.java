package top.jpower.jpower.log.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.jpower.jpower.module.base.annotation.LoaderService;
import top.jpower.jpower.module.common.deploy.service.DeployService;

import java.util.Properties;

/**
 * @ClassName DeployService
 * @Description TODO 启动参数配置
 * @Author 郭丁志
 * @Date 2020-08-19 16:22
 * @Version 1.0
 */
@LoaderService(DeployService.class)
public class LogDeployServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplicationBuilder builder, String appName, String profile) {
        Properties props = System.getProperties();
        props.setProperty("logging.config", "classpath:logback-spring.xml");
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
