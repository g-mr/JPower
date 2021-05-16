package com.wlcb.jpower.module.common.deploy.config;

import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import java.net.InetAddress;

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
			jpowerProperties.setHostName(getHostName(serverProperties.getAddress()));
			jpowerProperties.setIp(getHostAddress(serverProperties.getAddress()));
			jpowerProperties.setPort(serverProperties.getPort());
		}
	}

	private String getHostName(InetAddress address){
		if (Fc.isNull(address)){
			return "127.0.0.1";
		}
		return address.getHostName();
	}

	private String getHostAddress(InetAddress address){
		if (Fc.isNull(address)){
			return "127.0.0.1";
		}
		return address.getHostAddress();
	}
}
