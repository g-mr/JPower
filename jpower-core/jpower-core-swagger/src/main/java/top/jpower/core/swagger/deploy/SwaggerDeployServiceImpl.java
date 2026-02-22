package top.jpower.core.swagger.deploy;

import com.google.auto.service.AutoService;
import org.springframework.boot.SpringApplication;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.MapUtil;

import java.util.Map;

/**
 * 初始化Swagger配置
 * @author mr.g
 */
@AutoService(DeployService.class)
public class SwaggerDeployServiceImpl implements DeployService {
	@Override
	public void deploy(SpringApplication builder, ConfigurableEnvironment properties, String appName, String profile) {

		Map<String, Object> map = MapUtil.newHashMap();
		map.put("knife4j.enable", true);
		if (profile.equals(JpowerConstants.PROD_CODE)) {
			map.put("knife4j.production", true);
		}
		properties.getPropertySources().addLast(new MapPropertySource("swaggerDeployService", map));

	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
