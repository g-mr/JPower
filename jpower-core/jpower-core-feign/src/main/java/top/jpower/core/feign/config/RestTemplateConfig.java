package top.jpower.core.feign.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import top.jpower.core.feign.config.interceptor.RestTemplateInterceptor;

import static org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication.Type.SERVLET;

/**
 * @author mr.g
 * @date 2022-04-22 09:31
 */

@Slf4j
@AutoConfiguration
@ConditionalOnWebApplication(type = SERVLET)
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public RestTemplateInterceptor restTemplateInterceptor(){
        return new RestTemplateInterceptor();
    }

}
