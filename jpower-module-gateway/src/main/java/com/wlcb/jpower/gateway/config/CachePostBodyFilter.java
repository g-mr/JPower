package com.wlcb.jpower.gateway.config;

import io.netty.buffer.UnpooledByteBufAllocator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.NettyDataBufferFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author mr.g
 * @date 2022-06-01 12:11
 */
@Component
@ConditionalOnProperty(value = "jpower.gateway.log", havingValue = "true", matchIfMissing = false)
public class CachePostBodyFilter implements GlobalFilter, Ordered {

    public static final String CACHED_REQUEST_BODY = "CACHED_REQUEST_BODY";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerRequest serverRequest = ServerRequest.create(exchange, HandlerStrategies.withDefaults().messageReaders());
        Mono<String> bodyToMono = serverRequest.bodyToMono(String.class);
        if (requiresBody(exchange.getRequest().getMethod())){
            return bodyToMono.flatMap(body -> {
                exchange.getAttributes().put(CACHED_REQUEST_BODY, body);
                ServerHttpRequest newRequest = new ServerHttpRequestDecorator(exchange.getRequest()) {

                    @Override
                    public Flux<DataBuffer> getBody() {
                        NettyDataBufferFactory nettyDataBufferFactory = new NettyDataBufferFactory(new UnpooledByteBufAllocator(false));
                        DataBuffer bodyDataBuffer = nettyDataBufferFactory.wrap(body.getBytes());
                        return Flux.just(bodyDataBuffer);
                    }
                };
                return chain.filter(exchange.mutate().request(newRequest).build());
            });
        }

        return chain.filter(exchange);

    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private boolean requiresBody(HttpMethod method) {
        switch(method) {
            case PUT:
            case POST:
            case PATCH:
                return true;
            default:
                return false;
        }
    }

}
