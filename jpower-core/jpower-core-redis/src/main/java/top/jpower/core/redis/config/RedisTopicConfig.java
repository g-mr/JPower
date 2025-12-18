package top.jpower.core.redis.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ErrorHandler;
import top.jpower.core.redis.connection.RedisConnectionFactoryBroker;
import top.jpower.core.redis.handler.JpowerRedisTemplate;
import top.jpower.core.redis.topic.RedisTopic;
import top.jpower.core.redis.topic.RedisTopicListener;
import top.jpower.core.redis.topic.RedisTopicListenerAdapter;
import top.jpower.core.redis.topic.RedisTopicScannerConfigurer;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.AnnotationUtil;
import top.jpower.core.util.utils.ClassUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Redis Pub/Sub 处理
 *
 * @author mr.g
 * @date 2024-10-31 22:16
 * @description
 */
@AutoConfiguration(after = RedisConfig.class)
@ConditionalOnBean(value = RedisConnectionFactory.class, name = "redisTemplate")
public class RedisTopicConfig {

    @Bean
    @ConditionalOnMissingBean(RedisTopicScannerConfigurer.class)
    public RedisTopicScannerConfigurer redisTopicScannerConfigurer(BeanFactory beanFactory){
        RedisTopicScannerConfigurer configurer = new RedisTopicScannerConfigurer();
        if (!AutoConfigurationPackages.has(beanFactory)) {
            configurer.getBasePackages().add(ClassUtil.getPackage(SpringUtil.getMainClass()));
        } else {
            configurer.getBasePackages().addAll(AutoConfigurationPackages.get(beanFactory));
        }
        return configurer;
    }


    /**
     * 构造监听器
     *
     * @author mr.g
     * @param scannerConfigurer 扫描路径
     * @param errorHandler 错误处理
     * @param redisConnectionFactory Redis连接器
     * @param redisTemplate Redis处理器
     * @return org.springframework.data.redis.listener.RedisMessageListenerContainer
     **/
    @Bean
    @ConditionalOnMissingBean
    public RedisMessageListenerContainer redisContainer(RedisTopicScannerConfigurer scannerConfigurer,
                                                        @Autowired(required = false) ErrorHandler errorHandler,
                                                        RedisConnectionFactory redisConnectionFactory,
                                                        RedisTemplate<String, Object> redisTemplate) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();

        if (redisTemplate instanceof JpowerRedisTemplate){
            container.setConnectionFactory(new RedisConnectionFactoryBroker(redisConnectionFactory, ((JpowerRedisTemplate) redisTemplate).getRedisProperties(), ((JpowerRedisTemplate) redisTemplate).getRedisPrefixHandler()));
        } else {
            container.setConnectionFactory(redisConnectionFactory);
        }

        //noinspection unchecked
        Set<Class<? extends RedisTopicListener<?>>> list = scannerConfigurer.getBasePackages().stream()
                .flatMap(pkg -> ClassUtil.scanPackageBySuper(pkg, RedisTopicListener.class).stream())
                .filter(clz-> AnnotationUtil.hasAnnotation(clz, RedisTopic.class))
                .map(clz -> (Class<? extends RedisTopicListener<?>>) clz)
                .collect(Collectors.toSet());

        if (Fc.notNull(errorHandler)){
            container.setErrorHandler(errorHandler);
        }

        list.forEach(clz -> {
            RedisTopicListener<?> listener = initListener(clz);
            RedisTopic redisTopic = AnnotationUtil.getAnnotation(clz, RedisTopic.class);

            RedisTopicListenerAdapter listenerAdapter = new RedisTopicListenerAdapter(listener, redisTopic);
            if (Fc.equalsValue(redisTemplate.getKeySerializer().getTargetType().getName(), String.class.getName())){
                //noinspection unchecked
                listenerAdapter.setKeySerializer((RedisSerializer<String>) redisTemplate.getKeySerializer());
            } else {
                listenerAdapter.setKeySerializer(redisTemplate.getStringSerializer());
            }
            listenerAdapter.setValueSerializer(redisTemplate.getValueSerializer());

            container.addMessageListener(listenerAdapter, bindTopicName(redisTopic));
        });

        return container;
    }

    /**
     * 构造监听 Channel
     *
     * @author mr.g
     * @param redisTopic Channel注解
     * @return java.util.Collection<? extends org.springframework.data.redis.listener.Topic>
     **/
    private Collection<? extends Topic> bindTopicName(RedisTopic redisTopic) {
        return Arrays.stream(redisTopic.value())
                .<Topic>map(topic->{  // 因为AbstractTopic是私有的，所以显示指定接口
                    if (StringUtil.contains(topic, StringPool.ASTERISK)){
                        return PatternTopic.of(topic);
                    } else {
                        return ChannelTopic.of(topic);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 实例化监听类
     *
     * @author mr.g
     * @param listenerClz 监听Class
     * @return top.jpower.core.redis.topic.RedisTopicListener<?>
     **/
    private RedisTopicListener<?> initListener(Class<? extends RedisTopicListener<?>> listenerClz) {
        if (SpringUtil.isExistBean(listenerClz)){
            return SpringUtil.getBean(listenerClz);
        }
        return ReflectUtil.newInstance(listenerClz);
    }

}
