package top.jpower.core.deploy.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.Ordered;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.util.utils.Fc;

import java.net.InetAddress;

/**
 * 配置类
 *
 * @author mr.g
 */
@AutoConfiguration
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE)
@EnableConfigurationProperties(JpowerProperties.class)
public class JpowerDeployConfiguration {


	public JpowerDeployConfiguration(ServerProperties serverProperties, JpowerProperties jpowerProperties) {
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
