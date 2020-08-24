package com.wlcb.jpower.module.common.swagger;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import springfox.documentation.RequestHandler;

import java.util.List;

/**
 * @ClassName SwaggerConfigUtil
 * @Description TODO swagger工具类
 * @Author 郭丁志
 * @Date 2020-08-12 16:47
 * @Version 1.0
 */
public class SwaggerConfigUtil {

    public static Predicate<RequestHandler> basePackage(final List<String> basePackages) {
        return input -> declaringClass(input).transform(handlerPackage(basePackages)).or(true);
    }

    private static Function<Class<?>, Boolean> handlerPackage(final List<String> basePackages)     {
        return input -> {
            // 循环判断匹配
            for (String strPackage : basePackages) {
                boolean isMatch = input.getPackage().getName().startsWith(strPackage);
                if (isMatch) {
                    return true;
                }
            }
            return false;
        };
    }

    private static Optional<? extends Class<?>> declaringClass(RequestHandler input) {
        return Optional.fromNullable(input.declaringClass());
    }

}
