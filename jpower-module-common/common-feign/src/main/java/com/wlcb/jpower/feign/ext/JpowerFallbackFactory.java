package com.wlcb.jpower.feign.ext;

import feign.Target;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 默认 Fallback，避免写过多fallback类
 *
 * @param <T> 泛型标记
 * @author mr.g
 */
@RequiredArgsConstructor
public class JpowerFallbackFactory<T>  implements FallbackFactory<T> {

    private final Target<T> target;

    @Override
    public T create(Throwable cause) {
        final Class<T> targetType = target.type();
        Enhancer enhancer = new Enhancer();
        enhancer.setCallback(new JpowerFeignFallback<>(targetType, target.name(), cause));
        enhancer.setSuperclass(targetType);
        enhancer.setUseCache(true);
        return (T) enhancer.create();
    }

}
