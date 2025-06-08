package top.jpower.core.redis.config;

import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import top.jpower.core.redis.log.RedisLog;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.CodecRedisSerializer;

/**
 * Redis 日志
 *
 * @author mr.g
 * @date 2024-11-7 22:30
 */
@AutoConfiguration
@AutoConfigureAfter({RedisConfig.class, RedisAutoConfiguration.class, RedissonAutoConfigurationV2.class})
@ConditionalOnBean(RedisProperties.class)
public class RedisLogConfig {

    @Bean
    @ConditionalOnBean(name = "redisTemplate")
    @ConditionalOnMissingBean
    public RedisLog redisLog(RedisProperties redisProperties, CodecRedisSerializer redisSerializer){
        return new RedisLog(redisProperties, redisSerializer);
    }

}
