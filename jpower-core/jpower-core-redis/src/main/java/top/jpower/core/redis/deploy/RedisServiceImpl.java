package top.jpower.core.redis.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.Fc;

import java.util.Collections;

/**
 * 启动初始化
 *
 * @author mr.g
 * @date 2024-11-7 22:50
 */
@AutoService(DeployService.class)
public class RedisServiceImpl implements DeployService {

    @Override
    public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {
        if (Fc.equalsValue(profile, JpowerConstants.PROD_CODE)){
			properties.getPropertySources()
					.addLast(new MapPropertySource("redisDeploy", Collections.singletonMap("jpower.redis.log", false)));
        }
    }

}
