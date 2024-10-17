package top.jpower.core.redis.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * redis连接器生成
 *
 * @author mr.g
 * @date 2024-9-10 22:12
 */
@AutoConfiguration
public class RedisFactoryConfig {

    // todo 这里有很大得BUG 实际redis内部使用得不是LettuceConnectionFactory 而是JpowerLettuceConnectionFactory 导致连接池完全失效



    /**
     * 获取LettuceConnectionFactory
     *
     * @author mr.g
     * @param redisConnectionFactory 连接器
     * @return top.jpower.jpower.module.common.redis.JpowerRedisConnectionFactoryManage
     **/
    // @Bean
    // @ConditionalOnBean(LettuceConnectionFactory.class)
    // @ConditionalOnMissingBean
    // public RedisConnectionFactoryManage redisLettuce(LettuceConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
    //     return new RedisConnectionFactoryManage(new JpowerLettuceConnectionFactory(redisConnectionFactory.getConnection(), redisProperties, redisPrefixHandler));
    // }
    //
    // /**
    //  * 获取JedisConnectionFactory
    //  *
    //  * @author mr.g
    //  * @param redisConnectionFactory
    //  * @return top.jpower.jpower.module.common.redis.JpowerRedisConnectionFactoryManage
    //  **/
    // @Bean
    // @ConditionalOnBean(JedisConnectionFactory.class)
    // @ConditionalOnMissingBean
    // public RedisConnectionFactoryManage redisJedis(JedisConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
    //     return new RedisConnectionFactoryManage(new JpowerJedisConnectionFactory(redisConnectionFactory.getConnection() , redisProperties, redisPrefixHandler));
    // }

}
