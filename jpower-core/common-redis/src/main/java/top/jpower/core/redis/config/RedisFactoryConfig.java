// package top.jpower.core.redis.config;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.autoconfigure.AutoConfiguration;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
// import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
// import org.springframework.context.annotation.Bean;
// import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import top.jpower.core.redis.connection.JpowerJedisConnectionFactory;
// import top.jpower.core.redis.connection.JpowerLettuceConnectionFactory;
// import top.jpower.core.redis.connection.RedisConnectionFactoryManage;
// import top.jpower.core.redis.properties.RedisProperties;
//
// /**
//  * redis连接器生成
//  *
//  * @author mr.g
//  * @date 2024-9-10 22:12
//  */
// @AutoConfiguration
// public class RedisFactoryConfig {
//
//     /**
//      * 获取LettuceConnectionFactory
//      *
//      * @author mr.g
//      * @param redisConnectionFactory 连接器
//      * @return top.jpower.jpower.module.common.redis.JpowerRedisConnectionFactoryManage
//      **/
//      @Bean
//      @ConditionalOnBean(LettuceConnectionFactory.class)
//      @ConditionalOnMissingBean
//      public RedisConnectionFactoryManage redisLettuce(LettuceConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
//          return new RedisConnectionFactoryManage(new JpowerLettuceConnectionFactory(redisConnectionFactory, redisProperties, redisPrefixHandler));
//      }
//
//      /**
//       * 获取JedisConnectionFactory
//       *
//       * @author mr.g
//       * @param redisConnectionFactory
//       * @return top.jpower.jpower.module.common.redis.JpowerRedisConnectionFactoryManage
//       **/
//      @Bean
//      @ConditionalOnBean(JedisConnectionFactory.class)
//      @ConditionalOnMissingBean
//      public RedisConnectionFactoryManage redisJedis(JedisConnectionFactory redisConnectionFactory, RedisProperties redisProperties, @Autowired(required = false) RedisPrefixHandler redisPrefixHandler) {
//          return new RedisConnectionFactoryManage(new JpowerJedisConnectionFactory(redisConnectionFactory, redisProperties, redisPrefixHandler));
//      }
//
// }
