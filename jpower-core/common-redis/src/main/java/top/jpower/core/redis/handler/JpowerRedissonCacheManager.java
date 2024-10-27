package top.jpower.core.redis.handler;

import org.redisson.Redisson;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import top.jpower.core.redis.utils.CachePrefix;

import java.util.Map;

/**
 * @author mr.g
 * @date 2024-10-23 22:40
 * @description
 */
public class JpowerRedissonCacheManager extends RedissonSpringCacheManager {

    private Redisson rds;

    /**
     * Creates CacheManager supplied by Redisson instance
     *
     * @param redisson object
     */
    public JpowerRedissonCacheManager(Redisson redisson) {
        super(redisson);
        this.rds = redisson;
    }

    /**
     * Creates CacheManager supplied by Redisson instance and
     * Cache config mapped by Cache name
     *
     * @param redisson object
     * @param config object
     */
    public JpowerRedissonCacheManager(Redisson redisson, Map<String, ? extends CacheConfig> config) {
        super(redisson, config);
        this.rds = redisson;
    }


    @Override
    public Cache getCache(String name) {
        name = rds.getCommandExecutor().getServiceManager().getConfig().getNameMapper().map(name);
        CachePrefix.clear();
        return super.getCache(name);
    }
}
