package top.jpower.jpower.gateway.config;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.web.reactive.function.client.WebClientCustomizer;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.WebClient;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.jpower.gateway.utils.HttpRequestContextHolder;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

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
        return builder -> builder.filter((clientRequest, next) -> {
            return HttpRequestContextHolder.getRequest()
                    .flatMap(req -> {

                        HttpHeaders headers = new HttpHeaders();

                        req.getHeaders().forEach((name,value)->{
                            if (name.equalsIgnoreCase(AUTHORIZATION)
                                    || name.equalsIgnoreCase("User-Type")
                                    || name.equalsIgnoreCase(JpowerConstants.HEADER_MENU)
                                    || name.equalsIgnoreCase(JpowerConstants.AUTH_HEADER)
                                    || name.equalsIgnoreCase(JpowerConstants.HEADER_TENANT)){

                                headers.put(name, value);
                            }
                        });

                        // 创建新请求
                        ClientRequest newRequest = ClientRequest.from(clientRequest)
                                .headers(httpHeaders -> httpHeaders.addAll(headers))
                                .build();

                        return next.exchange(newRequest);
                    });
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
