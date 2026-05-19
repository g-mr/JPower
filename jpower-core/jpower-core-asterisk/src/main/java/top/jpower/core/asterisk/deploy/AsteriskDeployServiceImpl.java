package top.jpower.core.asterisk.deploy;

import com.google.auto.service.AutoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.asterisk.properties.AsteriskAmiProperties;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;

import java.util.Map;

/**
 * 启动初始化 asterisk
 *
 * @author mr.g
 */
@Slf4j
@AutoService(DeployService.class)
public class AsteriskDeployServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {

        AsteriskAmiProperties amiProperties = properties.getProperty("asterisk.ami", AsteriskAmiProperties.class);
        if (Fc.isNotEmpty(amiProperties)) {
            Map<String, Object> map = ChainMap.<String, Object>create()
                    .put("camel.component.asterisk.enabled", true)
                    .put("camel.component.asterisk.host", amiProperties.getHost())
                    .put("camel.component.asterisk.port", amiProperties.getPort())
                    .put("camel.component.asterisk.username", amiProperties.getUsername())
                    .put("camel.component.asterisk.password", amiProperties.getPassword())
                    .map();

            properties.getPropertySources().addLast(new MapPropertySource("asteriskDeployService", map));
        }
    }

}
