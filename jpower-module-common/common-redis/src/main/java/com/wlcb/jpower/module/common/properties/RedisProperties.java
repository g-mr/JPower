package com.wlcb.jpower.module.common.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.Map;

/**
 * Redis配置属性<br/>
 * 针对@Cacheable注解
 *
 * @author mr.g
 **/
@Data
@ConfigurationProperties(prefix = "jpower.redis")
public class RedisProperties {

    /**
     * 通过 @Cacheable 注解标注的方法的缓存策略
     */
    private Cache cacheable = new Cache();
    /**
     * 针对某几个具体的key配置
     */
    private Map<String, Cache> cacheableKey;

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
        private boolean cacheNullVal = true;

        /**
         * key 的前缀
         */
        private String keyPrefix;

        /**
         * 写入redis时，是否使用key前缀
         */
        private boolean useKeyPrefix = true;

    }

}
