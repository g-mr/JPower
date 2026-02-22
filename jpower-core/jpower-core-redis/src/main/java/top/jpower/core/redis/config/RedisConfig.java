package top.jpower.core.redis.config;

import org.redisson.client.codec.StringCodec;
import org.redisson.codec.CompositeCodec;
import org.redisson.codec.Kryo5Codec;
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
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.redis.handler.*;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.CodecRedisSerializer;
import top.jpower.core.redis.serializer.JpowerStringSerializer;

/**
 * Redis 配置类
 *
 * @author mr.g
 */
@EnableCaching
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfigurationV2.class})
public class RedisConfig {

    @Bean
    @ConditionalOnMissingBean
    public CodecRedisSerializer codecRedisSerializer(){
        return new CodecRedisSerializer(new CompositeCodec(new StringCodec(), new Kryo5Codec(), new Kryo5Codec()));
    }

    @Bean
    @ConditionalOnMissingBean
    public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer(CodecRedisSerializer redisSerializer,
                                                                                   RedisProperties redisProperties,
                                                                                   @Autowired(required = false) RedisPrefixHandler redisPrefixHandler){
        JpowerCustomizerRedissonHandler customizerRedissonConfig = new JpowerCustomizerRedissonHandler();
        customizerRedissonConfig.setNameMapper(new PrefixRedissonHandler(redisProperties.getPrefix(), redisPrefixHandler));
        customizerRedissonConfig.setCodec(redisSerializer);
        return customizerRedissonConfig;
    }

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory,
                                                       CodecRedisSerializer redisSerializer,
                                                       RedisProperties redisProperties,
                                                       @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
        RedisTemplate<String, Object> template = new JpowerRedisTemplate(redisConnectionFactory, redisProperties, redisPrefixHandler);

        // value 序列化
        template.setValueSerializer(redisSerializer.getValueRedisSerializer());
        template.setHashValueSerializer(redisSerializer.getValueRedisSerializer());
        // key 序列化
        template.setKeySerializer(redisSerializer.getKeyRedisSerializer());
        template.setHashKeySerializer(redisSerializer.getKeyRedisSerializer());

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
