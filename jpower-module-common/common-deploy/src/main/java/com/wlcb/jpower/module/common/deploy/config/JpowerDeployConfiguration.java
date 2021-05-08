package com.wlcb.jpower.module.common.deploy.config;

import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.NetUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 配置类
 *
 * @author mr.g
 */
@Configuration(proxyBeanMethods = false)
@Order(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties({
	JpowerProperties.class
})
public class JpowerDeployConfiguration  implements SmartInitializingSingleton {

	private final ServerProperties serverProperties;
	private final JpowerProperties jpowerProperties;

	@Autowired(required = false)
	public JpowerDeployConfiguration(ServerProperties serverProperties, JpowerProperties jpowerProperties) {
		this.serverProperties = serverProperties;
		this.jpowerProperties = jpowerProperties;
	}

	@Override
	public void afterSingletonsInstantiated() {
		if (Fc.notNull(serverProperties)){
			jpowerProperties.setHostName(NetUtil.getHostName(serverProperties.getAddress()));
			jpowerProperties.setIp(NetUtil.getHostIp(serverProperties.getAddress()));
			jpowerProperties.setPort(serverProperties.getPort());
		}
	}
}
