package com.wlcb.jpower.module.common.swagger;

import com.wlcb.jpower.module.common.deploy.service.DeployService;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.core.Ordered;

import java.util.Properties;

/**
 * 初始化Swagger配置
 * @author mr.g
 */
public class SwaggerLauncherServiceImpl implements DeployService {
	@Override
	public void launcher(SpringApplicationBuilder builder, String appName, String profile) {
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
