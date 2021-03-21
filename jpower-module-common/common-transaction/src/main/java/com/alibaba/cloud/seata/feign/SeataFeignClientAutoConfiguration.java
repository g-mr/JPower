package com.alibaba.cloud.seata.feign;

import com.wlcb.jpower.feign.ext.JpowerSentinelFeign;
import feign.Client;
import feign.Feign;
import feign.Retryer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * @ClassName SeataFeignClientAutoConfiguration
 * @Description TODO 重写SeataFeignClientAutoConfiguration以适配最新API
 * @Author 郭丁志
 * @Date 2021/3/10 0010 2:13
 * @Version 1.0
 */
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(Client.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class SeataFeignClientAutoConfiguration {

    @Bean
    @Scope("prototype")
    @ConditionalOnClass(name = "com.alibaba.csp.sentinel.SphU")
    @ConditionalOnProperty(name = "feign.sentinel.enabled", havingValue = "true")
    Feign.Builder feignSentinelBuilder(BeanFactory beanFactory) {
        return JpowerSentinelFeign.builder().retryer(Retryer.NEVER_RETRY)
                .client(new SeataFeignClient(beanFactory));
    }

    @Bean
    @ConditionalOnMissingBean
    @Scope("prototype")
    Feign.Builder feignBuilder(BeanFactory beanFactory) {
        return SeataFeignBuilder.builder(beanFactory);
    }

    @Configuration(proxyBeanMethods = false)
    @RequiredArgsConstructor
    protected static class FeignBeanPostProcessorConfiguration {
        private final LoadBalancerProperties properties;
        private final LoadBalancerClientFactory loadBalancerClientFactory;
        private final LoadBalancedRetryFactory loadBalancedRetryFactory;

        @Bean
        SeataBeanPostProcessor seataBeanPostProcessor(SeataFeignObjectWrapper seataFeignObjectWrapper) {
            return new SeataBeanPostProcessor(seataFeignObjectWrapper);
        }

        @Bean
        SeataContextBeanPostProcessor seataContextBeanPostProcessor(BeanFactory beanFactory) {
            return new SeataContextBeanPostProcessor(beanFactory);
        }

        @Bean
        SeataFeignObjectWrapper seataFeignObjectWrapper(BeanFactory beanFactory) {
            return new SeataFeignObjectWrapper(beanFactory, loadBalancedRetryFactory, properties, loadBalancerClientFactory);
        }
    }

}
