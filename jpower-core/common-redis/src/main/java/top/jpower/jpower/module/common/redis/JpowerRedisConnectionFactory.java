package top.jpower.jpower.module.common.redis;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author mr.g
 * @date 2024-9-10 22:12
 * @description
 */
@AutoConfiguration
public class JpowerRedisConnectionFactory {

    /**
     * 投机取巧的写法
     *
     * @author mr.g
     * @param redisConnectionFactory
     * @return top.jpower.jpower.module.common.redis.JpowerRedisConnectionFactory
     **/
    @Bean
    @ConditionalOnBean(LettuceConnectionFactory.class)
    @ConditionalOnMissingBean
    public JpowerRedis redisLettuce(LettuceConnectionFactory redisConnectionFactory) {
        return new JpowerRedis(new JpowerLettuceConnectionFactory(redisConnectionFactory.getConnection()));
    }

    @Bean
    @ConditionalOnBean(JedisConnectionFactory.class)
    @ConditionalOnMissingBean
    public JpowerRedis redisJedis(JedisConnectionFactory redisConnectionFactory) {
        return new JpowerRedis(new JpowerJedisConnectionFactory(redisConnectionFactory.getConnection()));
    }

}
