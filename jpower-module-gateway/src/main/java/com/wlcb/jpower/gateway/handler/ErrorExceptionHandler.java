package com.wlcb.jpower.gateway.handler;

import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.module.base.vo.ResponseData;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @ClassName ErrorExceptionHandler
 * @Description TODO 异常返回
 * @Author 郭丁志
 * @Date 2020/8/27 0027 1:58
 * @Version 1.0
 */
@Slf4j
@Order(-1)
@Configuration
public class ErrorExceptionHandler implements ErrorWebExceptionHandler {

    private static final String API_PATH = "/doc.html";

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        //参考AbstractErrorWebExceptionHandler
        if (exchange.getResponse().isCommitted()) {
            return Mono.error(ex);
        }

        ResponseData data = message(request,ex);
        response.setRawStatusCode(data.getCode());
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        return response.writeWith(Flux.just(response.bufferFactory().wrap(JSON.toJSONBytes(data))));
    }

    /**
     * 构建异常信息
     *
     * @param request
     * @param ex
     * @return
     */
    private ResponseData message(ServerHttpRequest request, Throwable ex) {
        if (Fc.isNull(ex)){
            return ReturnJsonUtil.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(),"系统异常,请联系管理员");
        }

        String uri = request.getURI().toString();
        if (uri.endsWith(API_PATH)) {
            return ReturnJsonUtil.fail(HttpStatus.NOT_FOUND.value(),"接口文档已迁移到【jpower-api】服务,请联系开发人员索要请求地址");
        }

        StringBuilder message = new StringBuilder("请求[");
        message.append(request.getMethodValue());
        message.append(" ");
        message.append(request.getURI());
        message.append("]失败 : ");

        int httpStatus = HttpStatus.BAD_GATEWAY.value();
        if (ex instanceof NotFoundException) {
            httpStatus = HttpStatus.NOT_FOUND.value();
            message.append("请求地址找不到");
        } else if(ex instanceof ResponseStatusException){
            ResponseStatusException responseStatusException = (ResponseStatusException) ex;
            httpStatus = responseStatusException.getStatus().value();
            message.append(ex.getMessage());
        }else if (ex instanceof RuntimeException) {
            Throwable cause = ex.getCause();
            message.append(ex.getMessage());
            if(null != cause && cause.getMessage().contains("Load balancer does not have available server for client")){
                message.append("服务不存在");
            }
        }else {
            message.append(ex.getMessage());
        }

        return ReturnJsonUtil.fail(httpStatus,message.toString());
    }

}
