package top.jpower.core.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.util.ArrayList;
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
     * 前缀配置
     **/
    private Prefix prefix = new Prefix();

    /**
     * 是否开启redis日志
     **/
    private Boolean log = true;

    /**
     * CacheManage的缓存策略
     */
    private CacheManager cacheable = new CacheManager();

    /**
     * CacheManage针对某几个具体的key配置
     */
    private Map<String, CacheManager> cacheableKey;


    @Data
    public static class Prefix {
        /**
         * 是否启用前缀生成器
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
         * key 的过期时间
         * 默认不过期
         */
        private Duration timeToLive = Duration.ZERO;

        /**
         * 是否允许缓存null值
         */
        private boolean cacheNullVal = true;

        /**
         * key 的前缀
         */
        private String keyPrefix;

    }
}
