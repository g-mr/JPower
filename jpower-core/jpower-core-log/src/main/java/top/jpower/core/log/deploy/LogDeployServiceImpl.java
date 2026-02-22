package top.jpower.core.log.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.utils.ChainMap;

import java.util.Map;

/**
 * 设置日志配置
 *
 * @author 郭丁志
 */
@AutoService(DeployService.class)
public class LogDeployServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {
		Map<String, Object> map = ChainMap.<String, Object>create()
				.put("logging.config", "classpath:logback-spring.xml")
				.put("spring.sleuth.web.filterOrder", Ordered.HIGHEST_PRECEDENCE)
				.map();
        properties.getPropertySources().addFirst(new MapPropertySource("logDeployService", map));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

}
