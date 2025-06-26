package top.jpower.core.feign.config;

import cn.hutool.core.util.StrUtil;
import feign.RequestInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import top.jpower.core.util.utils.Fc;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@AutoConfiguration
public class DynamicFeignConfig {

    public final static String SERVICE_PARAM_NAME = "serviceName";

    @Bean
    @ConditionalOnBean(DiscoveryClient.class)
    public RequestInterceptor dynamicRequestInterceptor(DiscoveryClient discoveryClient) {
        return template -> {

            Collection<String> serviceValues = template.queries().get(SERVICE_PARAM_NAME);
            if (Fc.isNotEmpty(serviceValues)) {
                String serviceName = serviceValues.iterator().next();

                // 创建新的可修改查询参数 Map
                Map<String, Collection<String>> newQueries = new LinkedHashMap<>(template.queries());
                // 移除目标参数
                newQueries.remove(SERVICE_PARAM_NAME);
                // 设置回模板
                template.queries(newQueries);

                List<ServiceInstance> instances = discoveryClient.getInstances(serviceName);
                if (instances.isEmpty()) {
                    throw new RuntimeException("Service unavailable: " + serviceName);
                }

                ServiceInstance instance = instances.get(ThreadLocalRandom.current().nextInt(instances.size()));
                String domain = instance.getHost() + ":" + instance.getPort();

                template.target(StrUtil.replace(template.feignTarget().url(), template.feignTarget().name(), domain));

            } else {
                log.warn("未发现{}参数，无法进行动态服务代理", SERVICE_PARAM_NAME);
            }

        };
    }

}
