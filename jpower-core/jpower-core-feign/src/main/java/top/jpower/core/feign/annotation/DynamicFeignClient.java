package top.jpower.core.feign.annotation;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AliasFor;
import top.jpower.core.feign.AutoFeignClientsRegistrar;
import top.jpower.core.feign.config.DynamicFeignConfig;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@FeignClient(configuration = DynamicFeignConfig.class)
@Import(AutoFeignClientsRegistrar.class) // 自动引入注册器
public @interface DynamicFeignClient {

    /**
     * 继承自 @FeignClient 的属性
     */
    @AliasFor(annotation = FeignClient.class, attribute = "contextId")
    String contextId() default "";

    @AliasFor(annotation = FeignClient.class, attribute = "configuration")
    Class<?>[] configuration() default {};

    @AliasFor(annotation = FeignClient.class, attribute = "fallback")
    Class<?> fallback() default void.class;

    @AliasFor(annotation = FeignClient.class, attribute = "fallbackFactory")
    Class<?> fallbackFactory() default void.class;

    /**
     * 新增属性：是否启用自动注册
     */
    boolean autoRegister() default true;

}
