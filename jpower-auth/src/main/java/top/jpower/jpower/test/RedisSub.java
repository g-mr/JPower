package top.jpower.jpower.test;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import top.jpower.core.redis.connection.RedisConnectionFactoryManage;

/**
 * @author mr.g
 * @date 2024-10-17 22:51
 * @description
 */
@Configuration
public class RedisSub {

    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new MySubcribe());
    }


    @Bean
    RedisMessageListenerContainer redisContainer(RedisConnectionFactoryManage redisConnectionFactoryManage) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactoryManage.getFactory());
        container.addMessageListener(messageListener(), new ChannelTopic("msg"));
        return container;
    }

    // @Bean
    // RedisMessageListenerContainer redisContainerGdz(RedisConnectionFactory factory) {
    //     final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
    //     container.setConnectionFactory(factory);
    //     container.addMessageListener(messageListener(), new ChannelTopic("gdz"));
    //     return container;
    // }


}
