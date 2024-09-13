package top.jpower.core.redis.connection;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;

/**
 * @author mr.g
 * @date 2024-9-10 0:27
 * @description
 */
@AllArgsConstructor
public class JpowerJedisConnectionFactory extends JedisConnectionFactory {
    private RedisConnection connection;

    @Override
    public RedisConnection getConnection() {
        return new JpowerRedisConnection(connection);
    }

}
