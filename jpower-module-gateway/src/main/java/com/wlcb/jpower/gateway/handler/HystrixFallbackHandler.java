package com.wlcb.jpower.gateway.handler;

import com.wlcb.jpower.module.common.support.ChainMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

/**
 * @ClassName HystrixFallbackHandler
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/27 0027 0:36
 * @Version 1.0
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {

    @Override
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        serverRequest.attribute(ServerWebExchangeUtils.GATEWAY_ORIGINAL_REQUEST_URL_ATTR)
                .ifPresent(originalUrls -> log.error("网关执行请求:{}失败,hystrix服务降级处理", originalUrls));

        Map<String,Object> map = ChainMap.init().set("code", HttpStatus.SERVICE_UNAVAILABLE.value())
                .set("message", "服务暂不可用，请稍后重试")
                .set("status", false);
        return ServerResponse
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .body(BodyInserters.fromValue(map));
    }
}