package top.jpower.jpower.module.common.swagger;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;
import top.jpower.jpower.module.base.annotation.LoaderService;
import top.jpower.jpower.module.common.deploy.service.DeployService;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

import java.util.Properties;

/**
 * 初始化Swagger配置
 * @author mr.g
 */
@LoaderService(DeployService.class)
public class SwaggerDeployServiceImpl implements DeployService {
	@Override
	public void deploy(SpringApplicationBuilder builder, String appName, String profile) {
		Properties props = System.getProperties();
		if (profile.equals(AppConstant.PROD_CODE)) {
			props.setProperty("knife4j.production", "true");
		}
	}

	@Override
	public int getOrder() {
		return Ordered.LOWEST_PRECEDENCE;
	}
}
