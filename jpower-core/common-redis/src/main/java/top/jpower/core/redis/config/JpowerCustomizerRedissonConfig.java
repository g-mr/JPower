package top.jpower.core.redis.config;

import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;

/**
 * @author mr.g
 * @date 2024-10-21 0:25
 * @description
 */
public class JpowerCustomizerRedissonConfig implements RedissonAutoConfigurationCustomizer {
    @Override
    public void customize(Config configuration) {
        // configuration.useSingleServer().setCommandMapper()


        // configuration.isSingleConfig()

        // configuration.na

        configuration.setCodec(new StringCodec());
    }
}
