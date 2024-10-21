package top.jpower.core.redis.config;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.RedissonSpringCacheManager;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.service.RedisService;

/**
 * @author mr.g
 * @date 2024-10-19 15:12
 * @description
 */
@EnableCaching
@AutoConfiguration
@EnableConfigurationProperties(RedisProperties.class)
@AutoConfigureBefore({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
@RequiredArgsConstructor
public class RedissonConfig {

    @Bean
    @ConditionalOnMissingBean(name = "redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // value 序列化
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.setHashValueSerializer(jackson2JsonRedisSerializer);
        // key 序列化
        StringRedisSerializer redisKeySerializer = new StringRedisSerializer();
        template.setKeySerializer(redisKeySerializer);
        template.setHashKeySerializer(redisKeySerializer);

        return template;
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisService redisUtils(RedisTemplate<String, Object> redisTemplate) {
        return new RedisService(redisTemplate);
    }

    /**
     * todo  1:使用AOP技术代理 CommandAsyncExecutor.async来控制入参的 键名的修改，达到自定义前缀的同事，满足删除的关联性
     * todo  2:实现线程之间的传递性，可以考虑在做redis操作之前，加一个前置操作，当执行了前置操作以后，redis的操作不需要添加前缀
     * todo  3:RedissonClient的操作不区分超级用户，删除也只能删除自己租户的；RedisTemplate操作做区分
     **/
     // @Bean
    // public RedissonClient redissonClient() {
    //     RedissonClient redissonClient = Redisson.create();
    //     // redissonClient.pre
    //     // redissonClient.commandExecutor
    //     redissonClient.getConfig().getConnectionListener()
    //     return redissonClient;
    // }

    @Bean
    public TestRedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizer(){
        return new TestRedissonAutoConfigurationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public CacheManager cacheManager(RedissonClient redissonClient) {
        return new RedissonSpringCacheManager(redissonClient);
    }

}
