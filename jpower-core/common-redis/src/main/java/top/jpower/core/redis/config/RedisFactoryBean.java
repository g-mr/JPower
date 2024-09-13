package top.jpower.core.redis.config;

import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.redis.utils.ProxyUtils;

/**
 * @author mr.g
 * @date 2021-05-26 11:12
 */
@Configuration
@ConditionalOnProperty(value = "jpower.redis.log", havingValue = "true", matchIfMissing = true)
public class RedisFactoryBean implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (beanName.equals("redisConnectionFactory")) {
            return ProxyUtils.getProxy(bean, invocation -> new RedisAdvice().interceptorRedisFactory(invocation));
        }
        return bean;
    }
}