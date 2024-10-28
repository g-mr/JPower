package top.jpower.core.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.connection.JpowerRedisConnection;
import top.jpower.core.redis.handler.JpowerRedissonCacheManager;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.util.utils.MapUtil;

import java.time.Duration;
import java.util.Map;
import java.util.Optional;

/**
 * @author mr.g
 * @date 2024-10-29 0:30
 * @description
 */
@AutoConfiguration
@AutoConfigureBefore({RedisAutoConfiguration.class})
@EnableConfigurationProperties(RedisProperties.class)
public class CacheManagerConfig {

    @Bean
    // @ConditionalOnMissingBean
    // @ConditionalOnClass(RedissonSpringCacheManager.class)
    // @ConditionalOnBean(Redisson.class)
    public CacheManager cacheManager(Redisson redissonClient, RedisProperties redisProperties) {
        JpowerRedissonCacheManager cacheManager = new JpowerRedissonCacheManager(redissonClient);
        cacheManager.setAllowNullValues(redisProperties.getCacheManager().getAllowNullValues());
        cacheManager.setConfig(redisProperties.getCacheManager().getKeys());
        return cacheManager;
    }

    @Bean
    @ConditionalOnBean(RedisConnectionFactory.class)
    @ConditionalOnMissingBean({CacheManager.class,RedissonClient.class})
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {

        RedisProperties.CacheManager cacheProperties = redisProperties.getCacheManager();

        Map<String, CacheConfig> configs = cacheProperties.getKeys();
        Map<String, RedisCacheConfiguration> map = MapUtil.newHashMap();
        //自定义的缓存过期时间配置
        Optional.ofNullable(configs).ifPresent(config ->
                config.forEach((key, cache) -> {
                    map.put(key, handleRedisCacheConfiguration(cache.getTTL(), cacheProperties.getAllowNullValues(), RedisCacheConfiguration.defaultCacheConfig()));
                })
        );

        return RedisCacheManager
                .builder(new RedisConnectionFactory() {
                    @Override
                    public RedisConnection getConnection() {
                        return new JpowerRedisConnection(redisConnectionFactory.getConnection(), redisProperties, redisPrefixHandler, RedisSerializer.string());
                    }

                    @Override
                    public RedisClusterConnection getClusterConnection() {
                        return redisConnectionFactory.getClusterConnection();
                    }

                    @Override
                    public boolean getConvertPipelineAndTxResults() {
                        return redisConnectionFactory.getConvertPipelineAndTxResults();
                    }

                    @Override
                    public RedisSentinelConnection getSentinelConnection() {
                        return redisConnectionFactory.getSentinelConnection();
                    }

                    @Override
                    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
                        return redisConnectionFactory.translateExceptionIfPossible(ex);
                    }
                })
                .cacheDefaults(handleRedisCacheConfiguration(0, cacheProperties.getAllowNullValues(), RedisCacheConfiguration.defaultCacheConfig()))
                .withInitialCacheConfigurations(map)
                .build();
    }

    private RedisCacheConfiguration handleRedisCacheConfiguration(long ttl, Boolean allowNullValues, RedisCacheConfiguration config) {
        if (ttl > 0) {
            config = config.entryTtl(Duration.ofMillis(ttl));
        }
        if (!allowNullValues) {
            config = config.disableCachingNullValues();
        }

        config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
        config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));

        return config;
    }

}
