package top.jpower.jpower.gateway.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.annotation.LoaderService;

import java.util.Properties;

/**
 * @author mr.g
 * @date 2024/2/26 9:56 AM
 */
@LoaderService(DeployService.class)
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
    public void deploy(SpringApplicationBuilder builder, Properties properties, String appName, String profile) {
        System.setProperty("csp.sentinel.app.type", "1");
    }
}
