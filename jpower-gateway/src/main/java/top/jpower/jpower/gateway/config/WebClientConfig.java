package top.jpower.jpower.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.client.loadbalancer.reactive.RetryableLoadBalancerExchangeFilterFunction;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {


    @Bean
    @LoadBalanced
    public WebClient webClient(WebClient.Builder builder, RetryableLoadBalancerExchangeFilterFunction filter){
        return builder
//                .filter(filter)
                .build();
    }

    @Bean
    public ExchangeFilterFunction filterFunction(){
        return (request, next) -> {
            System.out.println("进入到WebClient拦截器................");
            return next.exchange(request);
        };
    }

    @Bean
    public WebClientCustomizer webClientCustomizer(ObjectProvider<ExchangeFilterFunction> objectProvider){
        return builder -> {
            builder.filters(filters ->
                    objectProvider.orderedStream()
                            // todo DeferringLoadBalancerExchangeFilterFunction和RetryableLoadBalancerExchangeFilterFunction不能共存,不知道为什么
                            // todo 需要查一下原因
                            //.filter(filterFunction -> !(filterFunction instanceof DeferringLoadBalancerExchangeFilterFunction))
                            .filter(filterFunction -> !(filterFunction instanceof RetryableLoadBalancerExchangeFilterFunction))
                            .forEach(filters::add));
        };
    }

}
