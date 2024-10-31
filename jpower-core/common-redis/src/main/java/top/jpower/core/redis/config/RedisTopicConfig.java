package top.jpower.core.redis.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import top.jpower.core.redis.topic.RedisTopicScannerConfigurer;
import top.jpower.core.util.utils.ClassUtil;

/**
 * @author mr.g
 * @date 2024-10-31 22:16
 * @description
 */
@AutoConfiguration(after = RedisConfig.class)
@ConditionalOnBean(value = RedisConnectionFactory.class, name = "redisTemplate")
public class RedisTopicConfig {

    @Bean
    @ConditionalOnMissingBean
    public MessageListenerAdapter messageListener(RedisTemplate<String, Object> redisTemplate) {
        return new MessageListenerAdapter();
    }


    @Bean
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer redisContainer(@Autowired(required = false) RedisTopicScannerConfigurer scannerConfigurer, RedisConnectionFactory redisConnectionFactoryManage, RedisTemplate<String, Object> redisTemplate) {

        if (scannerConfigurer == null){
            ClassUtil.getMainClass();
        }


        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactoryManage);
        container.addMessageListener(new MessageListenerAdapter(), new ChannelTopic("123456:msg"));




        // ReflectUtil.newInstance();
        // ClassUtil.scanPackageByAnnotation();
        // ClassUtil.scanPackageBySuper(StringPool.EMPTY, LogConstant.class);

        // System.out.println(ClassUtil.scanPackageBySuper());

        return container;
    }

}
