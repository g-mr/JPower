package com.alibaba.cloud.seata.feign;

import feign.Client;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.FeignBlockingLoadBalancerClient;
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient;

/**
 * @ClassName SeataFeignObjectWrapper
 * @Description TODO 重写SeataFeignObjectWrapper以适配最新API
 * @Author goo
 * @Date 2021/3/10 0010 3:06
 * @Version 1.0
 */
public class SeataFeignObjectWrapper {
    private final BeanFactory beanFactory;
    private final LoadBalancedRetryFactory loadBalancedRetryFactory;
    private final LoadBalancerProperties properties;
    private final LoadBalancerClientFactory loadBalancerClientFactory;

//    private SpringClientFactory springClientFactory;

    SeataFeignObjectWrapper(BeanFactory beanFactory, LoadBalancedRetryFactory loadBalancedRetryFactory, LoadBalancerProperties properties, LoadBalancerClientFactory loadBalancerClientFactory) {
        this.beanFactory = beanFactory;
        this.loadBalancedRetryFactory = loadBalancedRetryFactory;
        this.properties = properties;
        this.loadBalancerClientFactory = loadBalancerClientFactory;
    }

    Object wrap(Object bean) {
        if (bean instanceof Client && !(bean instanceof SeataFeignClient)) {
            if (bean instanceof FeignBlockingLoadBalancerClient) {
                FeignBlockingLoadBalancerClient client = (FeignBlockingLoadBalancerClient) bean;
                return new SeataFeignBlockingLoadBalancerClient(client.getDelegate(),
                        beanFactory.getBean(BlockingLoadBalancerClient.class), this, properties, loadBalancerClientFactory);
            }
            if (bean instanceof RetryableFeignBlockingLoadBalancerClient) {
                RetryableFeignBlockingLoadBalancerClient client = (RetryableFeignBlockingLoadBalancerClient) bean;
                return new SeataRetryableFeignBlockingLoadBalancerClient(client.getDelegate(),
                        beanFactory.getBean(BlockingLoadBalancerClient.class), this, loadBalancedRetryFactory, properties, loadBalancerClientFactory);
            }
            return new SeataFeignClient(this.beanFactory, (Client) bean);
        }
        return bean;
    }

//    SpringClientFactory clientFactory() {
//        if (this.springClientFactory == null) {
//            this.springClientFactory = this.beanFactory
//                    .getBean(SpringClientFactory.class);
//        }
//        return this.springClientFactory;
//    }
}
