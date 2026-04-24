package top.jpower.core.auth.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;

import java.util.Collections;

import static top.jpower.core.auth.endpoint.BuiltEndpoint.PATH;

/**
 * 隐藏端点
 *
 * @author mr.g
 */
@AutoService(DeployService.class)
public class EndpointHiddenDeployServiceImpl implements DeployService {

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
        properties.getPropertySources()
                .addLast(new MapPropertySource("endpointHiddenDeployService",
                        Collections.singletonMap("springdoc.paths-to-exclude",
                                PATH)));
    }

}
