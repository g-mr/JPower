package com.wlcb.jpower.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * AdminConfiguration
 * @author ding
 */
@Configuration
public class AdminConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(value = "spring.cloud.nacos.discovery.watch.enabled", matchIfMissing = true)
	public NacosWatch nacosWatch(NacosDiscoveryProperties nacosDiscoveryProperties) {
		return new NacosWatch(nacosDiscoveryProperties);
	}

//	@Bean
//	@ConditionalOnMissingBean
//	@ConditionalOnProperty(value = "spring.boot.admin.notify.dingtalk.enabled", havingValue = "true")
//	public CustomNotifier customNotifier(InstanceRepository repository) {
//		return new CustomNotifier(repository);
//	}

}
