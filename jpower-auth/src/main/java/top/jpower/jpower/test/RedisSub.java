package top.jpower.jpower.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

/**
 * @author mr.g
 * @date 2024-10-17 22:51
 * @description
 */
@Configuration
public class RedisSub {

    @Bean
    MessageListenerAdapter messageListener(RedisTemplate<String, Object> redisTemplate) {
        return new MessageListenerAdapter(new MySubcribe(redisTemplate));
    }


    // @Bean
    // RedisMessageListenerContainer redisContainer(RedisConnectionFactory redisConnectionFactoryManage, RedisTemplate<String, Object> redisTemplate) {
    //     final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    //     container.setConnectionFactory(redisConnectionFactoryManage);
    //     container.addMessageListener(messageListener(redisTemplate), new ChannelTopic("msg"));
    //     return container;
    // }

    // @Bean
    // RedisMessageListenerContainer redisContainerGdz(RedisConnectionFactory factory) {
    //     final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    //     container.setConnectionFactory(factory);
    //     container.addMessageListener(messageListener(), new ChannelTopic("gdz"));
    //     return container;
    // }


}
