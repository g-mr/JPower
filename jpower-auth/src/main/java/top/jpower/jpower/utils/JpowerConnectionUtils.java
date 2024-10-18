package top.jpower.jpower.utils;


import org.springframework.data.redis.connection.ConnectionUtils;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

/**
 * @author mr.g
 * @date 2024/10/18 18:14
 */
public class JpowerConnectionUtils extends ConnectionUtils {

    public static boolean isAsync(RedisConnectionFactory connectionFactory) {
        return true;
    }

    public static boolean isLettuce(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof LettuceConnectionFactory;
    }

    public static boolean isJedis(RedisConnectionFactory connectionFactory) {
        return connectionFactory instanceof JedisConnectionFactory;
    }

}
