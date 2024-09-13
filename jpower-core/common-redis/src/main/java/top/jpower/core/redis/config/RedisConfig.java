package top.jpower.core.redis.config;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.jpower.core.redis.connection.RedisConnectionFactoryManage;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.service.RedisUtil;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.MapUtil;

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


    // todo 下次先研究重写的方式来实现，实在不行就使用RedisConnectionFactoryManage方式

    @Bean
    @ConditionalOnMissingBean
    // todo 除了这个方式，还可以研究重写RedisTemplate的preProcessConnection方式来实现
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactoryManage redisConnectionFactoryManage) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // value 序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactoryManage.getFactory());
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
    // todo 3.实在没办法可以采用这个方法，自定义实现RedisConnectionFactoryManage
    public CacheManager cacheManager(RedisConnectionFactoryManage redisConnectionFactoryManage) {

        Map<String, RedisProperties.Cache> configs = redisProperties.getCacheableKey();
        Map<String, RedisCacheConfiguration> map = MapUtil.newHashMap();
        //自定义的缓存过期时间配置
        Optional.ofNullable(configs).ifPresent(config ->
                config.forEach((key, cache) -> {
                    map.put(key, handleRedisCacheConfiguration(cache, RedisCacheConfiguration.defaultCacheConfig()));
                })
        );

        return RedisCacheManager
                .builder(redisConnectionFactoryManage.getFactory())
                .cacheDefaults(handleRedisCacheConfiguration(redisProperties.getCacheable(), RedisCacheConfiguration.defaultCacheConfig()))
                .withInitialCacheConfigurations(map)
                .build();
    }

    // todo 1.可以研究RedisCacheManager.getMissingCache或者RedisCacheManager.getCache方法得重写是否可以实现
    // todo 2.可以研究RedisCacheWriter重构中得方式实现
    // public CacheManager cacheManager(LettuceConnectionFactory connectionFactory) {
    //
    //     Map<String, RedisProperties.Cache> configs = redisProperties.getCacheableKey();
    //     Map<String, RedisCacheConfiguration> map = MapUtil.newHashMap();
    //     //自定义的缓存过期时间配置
    //     Optional.ofNullable(configs).ifPresent(config ->
    //             config.forEach((key, cache) -> {
    //                 map.put(key, handleRedisCacheConfiguration(cache, RedisCacheConfiguration.defaultCacheConfig()));
    //             })
    //     );
    //
    //     return RedisCacheManager
    //             .builder(new RedisCacheWriter() {
    //                 @Override
    //                 public void put(String name, byte[] key, byte[] value, Duration ttl) {
    //
    //                 }
    //
    //                 @Override
    //                 public byte[] get(String name, byte[] key) {
    //                     return new byte[0];
    //                 }
    //
    //                 @Override
    //                 public byte[] putIfAbsent(String name, byte[] key, byte[] value, Duration ttl) {
    //                     return new byte[0];
    //                 }
    //
    //                 @Override
    //                 public void remove(String name, byte[] key) {
    //
    //                 }
    //
    //                 @Override
    //                 public void clean(String name, byte[] pattern) {
    //
    //                 }
    //
    //                 @Override
    //                 public void clearStatistics(String name) {
    //
    //                 }
    //
    //                 @Override
    //                 public RedisCacheWriter withStatisticsCollector(CacheStatisticsCollector cacheStatisticsCollector) {
    //                     return null;
    //                 }
    //
    //                 @Override
    //                 public CacheStatistics getCacheStatistics(String cacheName) {
    //                     return null;
    //                 }
    //             })
    //             .cacheDefaults(handleRedisCacheConfiguration(redisProperties.getCacheable(), RedisCacheConfiguration.defaultCacheConfig()))
    //             .withInitialCacheConfigurations(map)
    //             .build().getCache();
    // }
}

