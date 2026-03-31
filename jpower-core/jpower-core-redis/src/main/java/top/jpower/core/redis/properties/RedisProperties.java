package top.jpower.core.redis.properties;

import lombok.Data;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.jpower.core.redis.handler.RedisPrefixHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * 是否开启redis日志
     **/
    private Boolean log = Boolean.TRUE;

    /**
     * 前缀配置
     **/
    private Prefix prefix = new Prefix();

    /**
     * CacheManage的缓存策略
     */
    private CacheManager cacheManager = new CacheManager();

    @Data
    public static class Prefix {
        /**
         * 是否启用前缀生成器,启用以后需要实现 {@link RedisPrefixHandler} 接口
         **/
        private Boolean enabled = Boolean.TRUE;

        /**
         * 哪些Key需要忽略前缀，支持蚂蚁匹配器
         **/
        private List<String> ignore = new ArrayList<>();

    }

    @Data
    public static class CacheManager {

        /**
         * 空值是否存储
         */
        private Boolean allowNullValues = Boolean.FALSE;

        /**
         * CacheManage针对某几个具体的key配置
         */
        private Map<String, CacheConfig> keys = new HashMap<>();
    }
}
