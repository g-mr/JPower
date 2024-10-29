package top.jpower.core.redis.handler;

import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.NameMapper;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.springframework.cache.Cache;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.utils.CachePrefix;
import top.jpower.core.util.utils.SpringUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author mr.g
 * @date 2024-10-23 22:40
 * @description
 */
public class JpowerRedissonCacheManager extends RedissonSpringCacheManager {

    @Setter
    NameMapper nameMapper;

    Map<String, CacheConfig> configMap = new ConcurrentHashMap<String, CacheConfig>();

    /**
     * Creates CacheManager supplied by Redisson instance
     *
     * @param redisson object
     */
    public JpowerRedissonCacheManager(RedissonClient redisson) {
        super(redisson);
        if (redisson instanceof Redisson) {
            nameMapper = ((Redisson) redisson).getCommandExecutor().getServiceManager().getConfig().getNameMapper();
        } else {
            nameMapper = new PrefixRedissonHandler(SpringUtil.getBean(RedisProperties.class).getPrefix(), SpringUtil.getBean(RedisPrefixHandler.class));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void setConfig(Map<String, ? extends CacheConfig> config) {
        this.configMap = (Map<String, CacheConfig>) config;
    }

    @Override
    public Cache getCache(String name) {
        String prefixName = nameMapper.map(name);

        CacheConfig config = configMap.get(name);
        if (config != null){
            configMap.put(prefixName, config);
            super.setConfig(configMap);
        }

        CachePrefix.clear();
        return super.getCache(prefixName);
    }
}
