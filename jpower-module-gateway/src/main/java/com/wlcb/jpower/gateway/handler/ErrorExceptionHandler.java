package com.wlcb.jpower.gateway.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wlcb.jpower.module.common.support.ChainMap;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @ClassName ErrorExceptionHandler
 * @Description TODO 异常返回
 * @Author 郭丁志
 * @Date 2020/8/27 0027 1:58
 * @Version 1.0
 */
@Order(-1)
@Configuration
@RequiredArgsConstructor
public class ErrorExceptionHandler implements ErrorWebExceptionHandler {

    private static final String API_PATH = "doc.html";

    private final ObjectMapper objectMapper;

    @NonNull
    @Override
    public Mono<Void> handle(ServerWebExchange exchange, @NonNull Throwable ex) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (response.isCommitted()) {
            return Mono.error(ex);
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }
        return response.writeWith(Mono.fromSupplier(() -> {
            DataBufferFactory bufferFactory = response.bufferFactory();
            try {
                HttpStatus status = HttpStatus.BAD_GATEWAY;
                if (ex instanceof ResponseStatusException) {
                    status = ((ResponseStatusException) ex).getStatus();
                }
                return bufferFactory.wrap(objectMapper.writeValueAsBytes(ChainMap.init().set("code",status.value()).set("message",this.buildMessage(request, ex)).set("status",false)));
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                return bufferFactory.wrap(new byte[0]);
            }
        }));
    }


    /**
     * 构建异常信息
     *
     * @param request
     * @param ex
     * @return
     */
    private String buildMessage(ServerHttpRequest request, Throwable ex) {

        String uri = request.getURI().toString();
        if (uri.endsWith(API_PATH)) {
            return "【聚合文档】已迁移至【jpower-api】服务实现，启动【jpower-api】服务并访问 [http://localhost:18000/doc.html]";
        }

        StringBuilder message = new StringBuilder("Failed to handle request [");
        message.append(request.getMethodValue());
        message.append(" ");
        message.append(request.getURI());
        message.append("]");
        if (ex != null) {
            message.append(": ");
            message.append(ex.getMessage());
        }
        return message.toString();
    }

}
