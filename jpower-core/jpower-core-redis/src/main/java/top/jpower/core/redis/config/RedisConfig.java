package top.jpower.core.redis.config;

import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.handler.JpowerCustomizerRedissonHandler;
import top.jpower.core.redis.handler.PrefixRedissonHandler;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.JpowerStringSerializer;
import top.jpower.core.redis.handler.JpowerRedisTemplate;
import top.jpower.core.redis.handler.JpowerStringRedisTemplate;
import top.jpower.core.redis.cache.RedisService;

/**
 * @author mr.g
 * @date 2024-10-19 15:12
 * @description
 */
@EnableCaching
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfigurationV2.class})
// @ConditionalOnBean(RedisConnectionFactory.class)
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisSerializer")
    public RedisSerializer<Object> redisSerializer(){
        return new Jackson2JsonRedisSerializer<>(Object.class);
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer(RedisSerializer<Object> redisSerializer,
                                                                                   RedisProperties redisProperties,
                                                                                   @Autowired(required = false) RedisPrefixHandler redisPrefixHandler){
        JpowerCustomizerRedissonHandler customizerRedissonConfig = new JpowerCustomizerRedissonHandler();
        customizerRedissonConfig.setKeySerializer(new JpowerStringSerializer());
        customizerRedissonConfig.setValueSerializer(redisSerializer);
        customizerRedissonConfig.setNameMapper(new PrefixRedissonHandler(redisProperties.getPrefix(), redisPrefixHandler));
        return customizerRedissonConfig;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       RedisSerializer<Object> redisSerializer,
                                                       RedisProperties redisProperties,
                                                       @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
        RedisTemplate<String, Object> template = new JpowerRedisTemplate(redisConnectionFactory, redisProperties, redisPrefixHandler);

        // value 序列化
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        // key 序列化
        JpowerStringSerializer redisKeySerializer = new JpowerStringSerializer();
        template.setKeySerializer(redisKeySerializer);
        template.setHashKeySerializer(redisKeySerializer);

        return template;
    }

    @Bean
    @ConditionalOnMissingBean(StringRedisTemplate.class)
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
        StringRedisTemplate template = new JpowerStringRedisTemplate(redisConnectionFactory, redisProperties, redisPrefixHandler);
        // key 序列化
        JpowerStringSerializer redisKeySerializer = new JpowerStringSerializer();
        template.setKeySerializer(redisKeySerializer);
        template.setHashKeySerializer(redisKeySerializer);

        template.setValueSerializer(redisKeySerializer);
        template.setHashValueSerializer(redisKeySerializer);
        return template;
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisService redisService(RedisTemplate<String, Object> redisTemplate) {
        return new RedisService(redisTemplate);
    }

}
