package com.alibaba.cloud.seata.feign;

import feign.Client;
import feign.Request;
import feign.Response;
import org.springframework.cloud.client.loadbalancer.LoadBalancedRetryFactory;
import org.springframework.cloud.client.loadbalancer.LoadBalancerProperties;
import org.springframework.cloud.loadbalancer.blocking.client.BlockingLoadBalancerClient;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.cloud.openfeign.loadbalancer.RetryableFeignBlockingLoadBalancerClient;

import java.io.IOException;

/**
 * @ClassName SeataRetryableFeignBlockingLoadBalancerClient
 * @Description TODO
 * @Author 郭丁志
 * @Date 2021/3/10 0010 3:07
 * @Version 1.0
 */
public class SeataRetryableFeignBlockingLoadBalancerClient extends RetryableFeignBlockingLoadBalancerClient {

    public SeataRetryableFeignBlockingLoadBalancerClient(Client delegate,
                                                         BlockingLoadBalancerClient loadBalancerClient,
                                                         SeataFeignObjectWrapper seataFeignObjectWrapper,
                                                         LoadBalancedRetryFactory loadBalancedRetryFactory,
                                                         LoadBalancerProperties properties,
                                                         LoadBalancerClientFactory loadBalancerClientFactory) {
        super((Client) seataFeignObjectWrapper.wrap(delegate), loadBalancerClient, loadBalancedRetryFactory, properties, loadBalancerClientFactory);
    }

    @Override
    public Response execute(Request request, Request.Options options) throws IOException {
        return super.execute(request, options);
    }

}
