package top.jpower.core.feign.config;

import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@AutoConfiguration
public class DynamicFeignConfig {

    public final static String SERVICE_PARAM_NAME = "serviceName";

    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    public RequestInterceptor dynamicRequestInterceptor(DiscoveryClient discoveryClient) {
        return template -> {
            // 从请求参数中获取服务名
            String serviceName = template.queries().get(SERVICE_PARAM_NAME).iterator().next();
            // 移除serviceName参数
            template.queries().remove(SERVICE_PARAM_NAME);

            List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
            if (instances.isEmpty()) {
                throw new RuntimeException("Service unavailable: " + serviceName);
            }

            ServiceInstance instance = instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
            String baseUrl = "http://" + instance.getHost() + ":" + instance.getPort();

            template.target(baseUrl);
        };
    }

}
