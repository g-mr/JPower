package top.jpower.jpower.module.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;
import top.jpower.jpower.module.common.properties.RedisProperties;

import java.util.Map;
import java.util.Optional;


/**
 * redis配置
 *
 * @author mr.g
 **/
@EnableCaching
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore({RedisAutoConfiguration.class})
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;


    @Bean
    @ConditionalOnMissingBean
    // @ConditionalOnSingleCandidate(RedisConnectionFactory.class)
    public RedisTemplate<String, Object> redisTemplate(JpowerRedis jpowerRedis) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // value 序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(jpowerRedis.getFactory());
        // key 序列化
        StringRedisSerializer redisKeySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);

        return redisTemplate;
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisUtil redisUtils(RedisTemplate<String, Object> redisTemplate) {
        return new RedisUtil(redisTemplate);
    }

    private RedisCacheConfiguration handleRedisCacheConfiguration(RedisProperties.Cache redisProperties, RedisCacheConfiguration config) {
        if (Fc.isNull(redisProperties)) {
            return config;
        }
        if (redisProperties.getTimeToLive() != null) {
            config = config.entryTtl(redisProperties.getTimeToLive());
        }
        if (redisProperties.getKeyPrefix() != null) {
            config = config.computePrefixWith(cacheName -> redisProperties.getKeyPrefix().concat(StringPool.COLON).concat(cacheName).concat(StringPool.COLON));
        } else {
            config = config.computePrefixWith(cacheName -> cacheName.concat(StringPool.COLON));
        }
        if (!redisProperties.isCacheNullVal()) {
            config = config.disableCachingNullValues();
        }
        if (!redisProperties.isUseKeyPrefix()) {
            config = config.disableKeyPrefix();
        }

        return config;
    }

    @Bean
    @Primary
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        Map<String, RedisProperties.Cache> configs = redisProperties.getCacheableKey();
        Map<String, RedisCacheConfiguration> map = MapUtil.newHashMap();
        //自定义的缓存过期时间配置
        Optional.ofNullable(configs).ifPresent(config ->
                config.forEach((key, cache) -> {
                    map.put(key, handleRedisCacheConfiguration(cache, RedisCacheConfiguration.defaultCacheConfig()));
                })
        );

        return RedisCacheManager
                // .builder(new RedisCacheWriter() {
                //     @Override
                //     public void put(String name, byte[] key, byte[] value, Duration ttl) {
                //
                //     }
                //
                //     @Override
                //     public byte[] get(String name, byte[] key) {
                //         return new byte[0];
                //     }
                //
                //     @Override
                //     public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
                //         return new byte[0];
                //     }
                //
                //     @Override
                //     public void remove(String name, byte[] key) {
                //
                //     }
                //
                //     @Override
                //     public void clean(String name, byte[] pattern) {
                //
                //     }
                //
                //     @Override
                //     public void clearStatistics(String name) {
                //
                //     }
                //
                //     @Override
                //     public RedisCacheWriter withStatisticsCollector(CacheStatisticsCollector cacheStatisticsCollector) {
                //         return null;
                //     }
                //
                //     @Override
                //     public CacheStatistics getCacheStatistics(String cacheName) {
                //         return null;
                //     }
                // })
                .builder(redisConnectionFactory)
                .cacheDefaults(handleRedisCacheConfiguration(redisProperties.getCacheable(), RedisCacheConfiguration.defaultCacheConfig()))
                .withInitialCacheConfigurations(map)
                .build();
    }
}

