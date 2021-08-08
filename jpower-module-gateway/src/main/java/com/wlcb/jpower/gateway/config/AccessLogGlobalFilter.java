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
//        Flux<DataBuffer> body = requestDecorator.getBody();
        //读取requestBody传参
        // TODO: 2021-05-25 这里试了很多方法都拿不到完整的requestBody，各位有什么办法能拿到可以提供一下 ！！！感谢🙏🙏🙏
//        AtomicReference<String> requestBody = new AtomicReference<>("");
//        body.subscribe(buffer -> {
//            CharBuffer charBuffer = StandardCharsets.UTF_8.decode(buffer.asByteBuffer());
//            requestBody.set(charBuffer.toString());
//        });
//        String requestParams = requestBody.get();

        StringBuilder builder = new StringBuilder(NEWLINE+"============start gateway http=============").append(NEWLINE);
        builder.append("-->")
                .append(route.getId()).append(SPACE)
                .append(method.name()).append(SPACE)
                .append(url.getScheme()).append(":/").append(getOriginalRequestUrl(requestDecorator)).append(SPACE)
                .append(NEWLINE);


        builder.append("request headers: ").append(NEWLINE);
        headers.forEach((name,value)-> builder.append(TAB).append(name).append(SPACE).append(EQUALS).append(SPACE).append(value).append(NEWLINE));

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
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private String readBody(byte[] content){
        try(Buffer buffer = new Buffer()){
            buffer.write(content);

            if (BufferUtil.isReadable(buffer)){
                return buffer.readString(CharsetKit.CHARSET_UTF_8);
            }
            return "omit bodyContent";
        }catch (Exception e){
            return "(unknown bodyContent)";
        }
    }

}
