package top.jpower.core.redis.connection;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.config.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;

/**
 * @author mr.g
 * @date 2024-9-10 0:27
 * @description
 */
@AllArgsConstructor
public class JpowerJedisConnectionFactory extends JedisConnectionFactory {
    private RedisConnection connection;
    private RedisProperties redisProperties;
    private RedisPrefixHandler redisPrefixHandler;
    private RedisSerializer<String> serializer;

    public JpowerJedisConnectionFactory(RedisConnection connection, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler){
        this(connection, redisProperties, redisPrefixHandler, RedisSerializer.string());
    }

    @Override
    public RedisConnection getConnection() {
        return new JpowerRedisConnection(connection, redisProperties, redisPrefixHandler, serializer);
    }

}
