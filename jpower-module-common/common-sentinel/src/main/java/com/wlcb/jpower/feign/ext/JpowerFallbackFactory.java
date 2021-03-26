package com.wlcb.jpower.feign.ext;

import feign.Target;
import lombok.AllArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * @ClassName JpowerFallbackFactory
 * @Description TODO 默认 Fallback
 * @Author 郭丁志
 * @Date 2021/3/10 0010 2:32
 * @Version 1.0
 */
@AllArgsConstructor
public class JpowerFallbackFactory<T>  implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    @SuppressWarnings("unchecked")
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        final String targetName = target.name();
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        enhancer.setCallback(new JpowerFeignFallback<>(targetType, targetName, cause));
        return (T) enhancer.create();
    }

}
