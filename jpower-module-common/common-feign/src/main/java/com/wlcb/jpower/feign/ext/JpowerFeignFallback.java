package com.wlcb.jpower.feign.ext;

import com.fasterxml.jackson.databind.JsonNode;
import com.wlcb.jpower.module.common.utils.JsonUtil;
import com.wlcb.jpower.module.common.utils.ObjectUtil;
import com.wlcb.jpower.module.common.utils.ReturnJsonUtil;
import feign.FeignException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.http.HttpStatus;

import javax.annotation.Nullable;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * jpower fallBack 代理处理
 *
 * @author mr.g
 */
@Slf4j
@AllArgsConstructor
public class JpowerFeignFallback<T> implements MethodInterceptor {
    private final Class<T> targetType;
    private final String targetName;
    private final Throwable cause;
    private final static String CODE = "code";

    @Nullable
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        String errorMessage = cause.getMessage();
        log.error("JpowerFeignFallback:[{}.{}] serviceId:[{}] message:[{}]", targetType.getName(), method.getName(), targetName, errorMessage);
        Class<?> returnType = method.getReturnType();
        // 暂时不支持 flux，rx，异步等，不是自定义的，直接返回 null。
        if (ReturnJsonUtil.class != returnType) {
            return null;
        }
        // 非 FeignException
        if (!(cause instanceof FeignException)) {
            return ReturnJsonUtil.printJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage,false);
        }
        FeignException exception = (FeignException) cause;
        byte[] content = exception.content();
        // 如果返回的数据为空
        if (ObjectUtil.isEmpty(content)) {
            return ReturnJsonUtil.printJson(HttpStatus.INTERNAL_SERVER_ERROR.value(), errorMessage,false);
        }
        JsonNode resultNode = JsonUtil.readTree(content);
        if (resultNode.has(CODE)) {
            return JsonUtil.getInstance().convertValue(resultNode, ReturnJsonUtil.class);
        }
        return ReturnJsonUtil.fail(resultNode.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        JpowerFeignFallback<?> that = (JpowerFeignFallback<?>) o;
        return targetType.equals(that.targetType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetType);
    }
}