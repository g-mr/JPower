package top.jpower.core.redis.topic;

import java.lang.annotation.*;

/**
 * Redis队列监听
 *
 * 继承 {@link RedisTopicListener} 实现接口,并注解{@link RedisTopic} 即可进行队列监听
 *
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RedisTopic {

    /**
     * 监听的topic名称
     * @return
     */
    String[] value();

    /**
     * 是否JSON结构
     * @return
     */
    boolean isJsonSource() default false;

}
