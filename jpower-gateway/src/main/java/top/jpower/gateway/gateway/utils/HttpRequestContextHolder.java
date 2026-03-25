package top.jpower.gateway.gateway.utils;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 获取上下文的Request Response
 *
 * @author mr.g
 * @date 2022-08-26 00:00
 */
public class HttpRequestContextHolder {

    public static final Class<ServerWebExchange> CONTEXT_KEY = ServerWebExchange.class;

    /**
     * 获取当前请求对象
     *
     * @author mr.g
     * @return reactor.core.publisher.Mono<org.springframework.http.server.reactive.ServerHttpRequest>
     **/
    public static Mono<ServerHttpRequest> getRequest() {
        return Mono.deferContextual(context -> Mono.just(context.get(CONTEXT_KEY).getRequest()));
    }

    /**
     * 获取当前response
     *
     * @author mr.g
     * @return reactor.core.publisher.Mono<org.springframework.http.server.reactive.ServerHttpResponse>
     **/
    public static Mono<ServerHttpResponse> getResponse(){
        return Mono.deferContextual(context -> Mono.just(context.get(CONTEXT_KEY).getResponse()));
    }

}
