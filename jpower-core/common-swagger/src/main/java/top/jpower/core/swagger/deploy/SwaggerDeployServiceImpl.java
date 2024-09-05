package top.jpower.core.swagger.deploy;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.jpower.core.deploy.service.DeployService;
import top.jpower.core.util.annotation.LoaderService;
import top.jpower.core.util.constants.JpowerConstants;

import java.util.Properties;

/**
 * 初始化Swagger配置
 * @author mr.g
 */
@LoaderService(DeployService.class)
public class SwaggerDeployServiceImpl implements DeployService {
	@Override
	public void deploy(SpringApplicationBuilder builder, Properties properties, String appName, String profile) {
		Properties props = System.getProperties();

		props.setProperty("knife4j.enable", "true");
		props.setProperty("spring.mvc.pathmatch.matching-strategy", "ANT_PATH_MATCHER");

		if (profile.equals(JpowerConstants.PROD_CODE)) {
			props.setProperty("knife4j.production", "true");
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
