package top.jpower.core.redis.connection;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author mr.g
 * @date 2024-9-10 0:27
 * @description
 */
@AllArgsConstructor
public class JpowerLettuceConnectionFactory extends LettuceConnectionFactory {
    private RedisConnection connection;

    @Override
    public RedisConnection getConnection() {
        return new JpowerRedisConnection(connection);
    }

}
