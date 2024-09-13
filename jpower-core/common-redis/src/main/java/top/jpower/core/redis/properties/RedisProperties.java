package top.jpower.core.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
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

    // 是否启用前缀生成器

    // 哪些Key需要忽略前缀（蚂蚁匹配器） 增删查全部忽略

    // 前缀生成规则（获取前缀）

    // 删除缓存的时候 前缀忽略规则（e.g：超级用户操作忽略前缀）

    /**
     * 前缀配置
     **/
    private Prefix prefix;

    /**
     * 是否开启redis日志
     **/
    private Boolean log = true;


    @Data
    public static class Prefix {

        /**
         * 是否启用前缀生成器
         **/
        private Boolean enabled = Boolean.TRUE;

        /**
         * 哪些Key需要忽略前缀
         **/
        private List<String> ignore;
    }






















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
