package top.jpower.core.redis.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfigurationV2;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.redis.lock.LockAspect;
import top.jpower.core.redis.lock.LockHandler;
import top.jpower.core.redis.lock.RedisLockHandler;
import top.jpower.core.redis.lock.RedissonLockHandler;

/**
 * @author mr.g
 * @date 2024-11-5 22:35
 * @description
 */
@AutoConfiguration
@AutoConfigureAfter({RedisConfig.class, RedisAutoConfiguration.class, RedissonAutoConfigurationV2.class})
public class RedisLockConfig {

    @Bean
    @ConditionalOnBean(RedissonClient.class)
    @ConditionalOnMissingBean(LockHandler.class)
    public LockHandler redissonLockHandler(RedissonClient redissonClient){
        return new RedissonLockHandler(redissonClient);
    }

    @Bean
    @ConditionalOnBean(RedisService.class)
    @ConditionalOnMissingBean(LockHandler.class)
    public LockHandler redisLockHandler(RedisService redisService){
        return new RedisLockHandler(redisService);
    }

    @Bean
    public LockAspect lockAspect(LockHandler lockHandler){
        return new LockAspect(lockHandler);
    }

}
