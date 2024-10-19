// package top.jpower.core.redis.config;
//
// import lombok.RequiredArgsConstructor;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.boot.autoconfigure.AutoConfigureBefore;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
// import org.springframework.boot.context.properties.EnableConfigurationProperties;
// import org.springframework.cache.CacheManager;
// import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.context.annotation.Bean;
// import org.springframework.data.redis.cache.RedisCacheConfiguration;
// import org.springframework.data.redis.cache.RedisCacheManager;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.core.StringRedisTemplate;
// import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializationContext;
// import org.springframework.data.redis.serializer.RedisSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;
// import top.jpower.core.redis.connection.RedisConnectionFactoryManage;
// import top.jpower.core.redis.properties.RedisProperties;
// import top.jpower.core.redis.service.RedisService;
// import top.jpower.core.util.constants.StringPool;
// import top.jpower.core.util.utils.Fc;
// import top.jpower.core.util.utils.MapUtil;
//
// import java.util.Map;
// import java.util.Optional;
//
//
// /**
//  * redis配置
//  *
//  * @author mr.g
//  **/
// @EnableCaching
// @AutoConfiguration
// @EnableConfigurationProperties(RedisProperties.class)
// @AutoConfigureBefore({RedisAutoConfiguration.class})
// @RequiredArgsConstructor
// public class RedisConfig {
//
//     @Bean
//     @ConditionalOnMissingBean(name = "redisTemplate")
//     public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactoryManage redisConnectionFactoryManage) {
//
//         RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//         redisTemplate.setConnectionFactory(redisConnectionFactoryManage.getFactory());
//
//         // value 序列化
//         Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
//         redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//         redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//         // key 序列化
//         StringRedisSerializer redisKeySerializer = new StringRedisSerializer();
//         redisTemplate.setKeySerializer(redisKeySerializer);
//         redisTemplate.setHashKeySerializer(redisKeySerializer);
//
//         return redisTemplate;
//     }
//
//      @Bean
//      @ConditionalOnMissingBean
//      public StringRedisTemplate stringRedisTemplate(RedisConnectionFactoryManage redisConnectionFactoryManage) {
//          return new StringRedisTemplate(redisConnectionFactoryManage.getFactory());
//      }
//
//     @Bean
//     @ConditionalOnBean(RedisTemplate.class)
//     public RedisService redisUtils(RedisTemplate<String, Object> redisTemplate) {
//         return new RedisService(redisTemplate);
//     }
//
//      @Bean
//      @ConditionalOnMissingBean
//      public CacheManager cacheManager(RedisConnectionFactoryManage redisConnectionFactoryManage, RedisProperties redisProperties) {
//
//          Map<String, RedisProperties.CacheManager> configs = redisProperties.getCacheableKey();
//          Map<String, RedisCacheConfiguration> map = MapUtil.newHashMap();
//          //自定义的缓存过期时间配置
//          Optional.ofNullable(configs).ifPresent(config ->
//                  config.forEach((key, cache) -> {
//                      map.put(key, handleRedisCacheConfiguration(cache, RedisCacheConfiguration.defaultCacheConfig()));
//                  })
//          );
//
//
//          return RedisCacheManager
//                  .builder(redisConnectionFactoryManage.getFactory())
//                  .cacheDefaults(handleRedisCacheConfiguration(redisProperties.getCacheable(), RedisCacheConfiguration.defaultCacheConfig()))
//                  .withInitialCacheConfigurations(map)
//                  .build();
//      }
//
//     private RedisCacheConfiguration handleRedisCacheConfiguration(RedisProperties.CacheManager redisProperties, RedisCacheConfiguration config) {
//         if (Fc.isNull(redisProperties)) {
//             return config;
//         }
//         if (redisProperties.getTimeToLive() != null) {
//             config = config.entryTtl(redisProperties.getTimeToLive());
//         }
//         if (Fc.isNotBlank(redisProperties.getKeyPrefix())) {
//             config = config.computePrefixWith(cacheName -> redisProperties.getKeyPrefix().concat(StringPool.COLON).concat(cacheName).concat(StringPool.COLON));
//         } else {
//             config = config.computePrefixWith(cacheName -> cacheName.concat(StringPool.COLON));
//         }
//         if (!redisProperties.isCacheNullVal()) {
//             config = config.disableCachingNullValues();
//         }
//
//         config.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()));
//         config.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(Object.class)));
//
//         return config;
//     }
//
// }
//
