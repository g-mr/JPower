package top.jpower.jpower.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * WebClient配置
 * 负载均衡设置
 *
 * @author mr.g
 **/
@Configuration(proxyBeanMethods = false)
public class WebClientConfig {


    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder.build();
    }

    @Bean
    public WebClientCustomizer webClientCustomizer(){
        return builder -> builder.filter((request, next) -> {
            System.out.println("进入到WebClient拦截器................");
            return next.exchange(request);
        });
    }

    @Bean
    @LoadBalanced
    public WebClient.Builder webClientBuilder(ObjectProvider<WebClientCustomizer> customizerProvider) {
        WebClient.Builder builder = WebClient.builder();
        customizerProvider.orderedStream().forEach((customizer) -> customizer.customize(builder));
        return builder;
    }

}
