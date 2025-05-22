package top.jpower.core.redis.handler;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.redisson.api.NameMapper;
import org.redisson.config.*;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.serializer.JpowerJsonJacksonCodec;
import top.jpower.core.redis.serializer.JpowerStringSerializer;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ReflectUtil;

/**
 * @author mr.g
 * @date 2024-10-21 0:25
 * @description
 */
@SuppressWarnings("unchecked")
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class JpowerCustomizerRedissonHandler implements RedissonAutoConfigurationCustomizer {

    @SuppressWarnings("rawtypes")
    private RedisSerializer keySerializer = new JpowerStringSerializer();
    @SuppressWarnings("rawtypes")
    private RedisSerializer valueSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
    private NameMapper nameMapper;

    @Override
    public void customize(Config configuration) {
        if (nameMapper != null){
            SingleServerConfig singleServerConfig = (SingleServerConfig) ReflectUtil.getFieldValue(configuration, "singleServerConfig");
            ClusterServersConfig clusterServersConfig = (ClusterServersConfig) ReflectUtil.getFieldValue(configuration, "clusterServersConfig");
            MasterSlaveServersConfig masterSlaveServersConfig = (MasterSlaveServersConfig) ReflectUtil.getFieldValue(configuration, "masterSlaveServersConfig");
            SentinelServersConfig sentinelServersConfig = (SentinelServersConfig) ReflectUtil.getFieldValue(configuration, "sentinelServersConfig");
            ReplicatedServersConfig replicatedServersConfig = (ReplicatedServersConfig) ReflectUtil.getFieldValue(configuration, "replicatedServersConfig");

            if (Fc.notNull(singleServerConfig)){
                singleServerConfig.setNameMapper(nameMapper);
            }
            if (Fc.notNull(clusterServersConfig)){
                clusterServersConfig.setNameMapper(nameMapper);
            }
            if (Fc.notNull(masterSlaveServersConfig)){
                masterSlaveServersConfig.setNameMapper(nameMapper);
            }
            if (Fc.notNull(sentinelServersConfig)){
                sentinelServersConfig.setNameMapper(nameMapper);
            }
            if (Fc.notNull(replicatedServersConfig)){
                replicatedServersConfig.setNameMapper(nameMapper);
            }
        }

        configuration.setCodec(new JpowerJsonJacksonCodec(keySerializer, valueSerializer));
        // configuration.setCodec(new ProtobufCodec(Object.class));
        // configuration.setCodec(new FittenCodec(Object.class));
        // configuration.setCodec(new Kryo5Codec(Object.class));
        // configuration.setCodec(new KryoCodec(Object.class));
    }
}
