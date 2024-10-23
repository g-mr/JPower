package top.jpower.core.redis.connection;

import org.redisson.Redisson;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;

import java.util.Map;

/**
 * @author mr.g
 * @date 2024-10-23 22:40
 * @description
 */
public class JpowerSpringCacheManager extends RedissonSpringCacheManager {

    Redisson redisson;

    public JpowerSpringCacheManager(Redisson redisson) {
        super(redisson);
        this.redisson = redisson;
    }

    /**
     * Creates CacheManager supplied by Redisson instance and
     * Cache config mapped by Cache name
     *
     * @param redisson object
     * @param config object
     */
    public JpowerSpringCacheManager(Redisson redisson, Map<String, ? extends CacheConfig> config) {
        super(redisson, config, null);
        this.redisson = redisson;
    }


    // todo 这种写法需要试试clear()的时候是把多个租户全部清空了，还是只清空当前租户
    // todo 还需要试试集群和哨兵模式是否有问题

    @Override
    public Cache getCache(String name) {
        Cache cache = instanceMap.get(name);
        if (cache != null) {
            return cache;
        }
        if (!dynamic) {
            return cache;
        }

        String newName = redisson.getCommandExecutor().getServiceManager().getConfig().getNameMapper().map(name);
        cache = instanceMap.get(newName);
        if (cache != null) {
            return cache;
        }
        if (!dynamic) {
            return cache;
        }

        CacheConfig config = configMap.get(name);
        if (config == null) {
            config = createDefaultConfig();
            configMap.put(name, config);
        }

        if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
            return createMap(name, newName, config);
        }

        return createMapCache(name, config);
    }

    private Cache createMap(String name, String newName, CacheConfig config) {
        RMap<Object, Object> map = getMap(name, config);

        Cache cache = new RedissonCache(map, allowNullValues);
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(newName, cache);
        if (oldCache != null) {
            cache = oldCache;
        }
        return cache;
    }

    private Cache createMapCache(String name, String newName, CacheConfig config) {
        RMapCache<Object, Object> map = getMapCache(name, config);

        Cache cache = new RedissonCache(map, config, allowNullValues);
        if (transactionAware) {
            cache = new TransactionAwareCacheDecorator(cache);
        }
        Cache oldCache = instanceMap.putIfAbsent(newName, cache);
        if (oldCache != null) {
            cache = oldCache;
        } else {
            map.setMaxSize(config.getMaxSize());
        }
        return cache;
    }

}
