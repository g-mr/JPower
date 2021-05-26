package com.wlcb.jpower.module.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/5/7 0007 23:24
 */
@Data
@ConfigurationProperties(prefix = "jpower.redis")
public class RedisProperties {

    /**
     * 是否缓存 null 值
     */
    private boolean cacheNullVal = true;

    /**
     * 通过 @Cacheable 注解标注的方法的缓存策略
     */
    private Cache def = new Cache();
    /**
     * 针对某几个具体的key特殊配置
     * <p>
     * 改属性只对 redis 有效！！！
     * configs的key需要配置成@Cacheable注解的value
     */
    private Map<String, Cache> configs;

    @Data
    public static class Cache {

        /**
         * key 的过期时间
         * 默认1天过期
         */
        private Duration timeToLive = Duration.ofDays(1);

        /**
         * 是否允许缓存null值
         */
        private boolean cacheNullValues = true;

        /**
         * key 的前缀
         * 最后的key格式： keyPrefix + @Cacheable.value + @Cacheable.key
         * <p>
         */
        private String keyPrefix;

        /**
         * 写入redis时，是否使用key前缀
         */
        private boolean useKeyPrefix = true;

        /**
         * Caffeine 的最大缓存个数
         */
        private int maxSize = 1000;

    }

}
