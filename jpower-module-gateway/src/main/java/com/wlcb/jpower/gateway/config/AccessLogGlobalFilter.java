package com.wlcb.jpower.gateway.config;

import com.wlcb.jpower.gateway.reactive.RecorderServerHttpRequestDecorator;
import com.wlcb.jpower.module.common.utils.BufferUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okio.Buffer;
import org.reactivestreams.Publisher;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.wlcb.jpower.gateway.config.CachePostBodyFilter.CACHED_REQUEST_BODY;
import static com.wlcb.jpower.module.common.utils.constants.StringPool.*;

/**
 * @author mr.g
 * @date 2021-05-24 10:55
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(value = "jpower.gateway.log", havingValue = "true", matchIfMissing = false)
public class AccessLogGlobalFilter implements GlobalFilter, Ordered {

    private final WebEndpointProperties endpointProperties;

    /**
     * 获取url
     *
     * @param requestDecorator
     * @return
     */
    public static String getOriginalRequestUrl(RecorderServerHttpRequestDecorator requestDecorator) {
        URI requestUri = requestDecorator.getURI();
        MultiValueMap<String, String> queryParams = requestDecorator.getQueryParams();
        return UriComponentsBuilder.fromPath(requestUri.getRawPath()).queryParams(queryParams).build().toUriString();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();

        // 忽略 endpoint 请求
        String endpointBasePath = endpointProperties.getBasePath();
        if (Fc.isNotBlank(endpointBasePath) && request.getPath().pathWithinApplication().value().startsWith(endpointBasePath)) {
            return chain.filter(exchange);
        }

        Route route = (Route) exchange.getAttributes().get(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        RecorderServerHttpRequestDecorator requestDecorator = new RecorderServerHttpRequestDecorator(request);
        HttpMethod method = requestDecorator.getMethod();
        URI url = requestDecorator.getURI();
        HttpHeaders headers = requestDecorator.getHeaders();

        StringBuilder builder = new StringBuilder(NEWLINE+"============start gateway http=============").append(NEWLINE);
        builder.append("-->")
                .append(route.getId()).append(SPACE)
                .append(method.name()).append(SPACE)
                .append(url.getScheme()).append(":/").append(getOriginalRequestUrl(requestDecorator)).append(SPACE)
                .append(NEWLINE);


        builder.append("request headers: ").append(NEWLINE);
        headers.forEach((name,value)-> builder.append(TAB).append(name).append(SPACE).append(EQUALS).append(SPACE).append(value).append(NEWLINE));

        if (Fc.isNotBlank(exchange.getAttributeOrDefault(CACHED_REQUEST_BODY,EMPTY))){
            builder.append("request body: ").append(NEWLINE);
            builder.append(TAB)
                    .append(exchange.getAttributeOrDefault(CACHED_REQUEST_BODY,EMPTY))
                    .append(NEWLINE);
            exchange.getAttributes().remove(CACHED_REQUEST_BODY);
        }

        builder.append("--> end request").append(NEWLINE);
        builder.append(NEWLINE);

        ServerHttpResponse response = exchange.getResponse();

        DataBufferFactory bufferFactory = response.bufferFactory();
        ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(response) {
            @Override
            public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                if (body instanceof Flux) {
                    Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
                    return super.writeWith(fluxBody.map(dataBuffer -> {

                        byte[] content = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(content);
                        String responseResult = readBody(content);
                        // 释放资源
                        DataBufferUtils.release(dataBuffer);

                        builder.append("<--")
                                .append(url.getScheme()).append(SPACE)
                                .append(this.getStatusCode()).append(SPACE)
                                .append(LEFT_BRACKET).append(System.currentTimeMillis() - startTime).append("ms").append(RIGHT_BRACKET).append(SPACE)
                                .append(NEWLINE);


                        builder.append("response headers: ").append(NEWLINE);
                        this.getHeaders().forEach((key,value) -> builder.append(TAB).append(key).append(SPACE).append(EQUALS).append(SPACE).append(value).append(NEWLINE));

                        if (Fc.isNotBlank(responseResult)){
                            builder.append("response body: ").append(NEWLINE);
                            builder.append(TAB)
                                    .append(responseResult)
                                    .append(NEWLINE);
                        }else {
                            builder.append("response body is null").append(NEWLINE);
                        }

                        builder.append("<-- end response").append(LEFT_BRACKET).append(responseResult.getBytes().length).append("-byte body").append(RIGHT_BRACKET).append(NEWLINE);
                        builder.append("============end gateway http=============").append(NEWLINE);

                        log.info(builder.toString());
                        return bufferFactory.wrap(content);
                    }));
                }
                return super.writeWith(body);
            }
        };

        return chain.filter(exchange.mutate().request(requestDecorator).response(decoratedResponse).build());
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE+1;
    }

    private String readBody(byte[] content){
        try(Buffer buffer = new Buffer()){
            buffer.write(content);
            return BufferUtil.read(buffer,CharsetKit.CHARSET_UTF_8);
        }catch (Exception e){
            return "(unknown bodyContent)";
        }
    }

    private Map<String, Object> decodeBody(String body) {
        return Arrays.stream(body.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(arr -> arr[0], arr -> arr[1]));
    }

    private String encodeBody(Map<String, Object> map) {
        return map.entrySet().stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
    }

}
