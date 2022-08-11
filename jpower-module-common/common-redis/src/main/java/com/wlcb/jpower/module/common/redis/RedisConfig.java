package com.wlcb.jpower.module.common.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.wlcb.jpower.module.common.properties.RedisProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.MapUtil;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Map;
import java.util.Optional;


/**
 * redis配置
 *
 * @author mr.g
 **/
@EnableCaching
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore({RedisAutoConfiguration.class})
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;

    @Bean(name = "redisTemplate")
    @ConditionalOnMissingBean(RedisTemplate.class)
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // value 序列化
        FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
        redisTemplate.setValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setHashValueSerializer(fastJsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // key 序列化
        StringRedisSerializer redisKeySerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(redisKeySerializer);
        redisTemplate.setHashKeySerializer(redisKeySerializer);

        return redisTemplate;
    }

    @Bean(name = "redisUtil")
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

    @Bean(name = "cacheManager")
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
                .builder(redisConnectionFactory)
                .cacheDefaults(handleRedisCacheConfiguration(redisProperties.getCacheable(), RedisCacheConfiguration.defaultCacheConfig()))
                .withInitialCacheConfigurations(map)
                .build();
    }
}

