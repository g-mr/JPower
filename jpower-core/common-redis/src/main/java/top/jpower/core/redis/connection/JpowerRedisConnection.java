package top.jpower.core.redis.connection;

import org.springframework.data.redis.connection.DefaultStringRedisConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * @author mr.g
 * @date 2024-9-10 0:34
 * @description
 */
public class JpowerRedisConnection extends DefaultStringRedisConnection {

    private static final byte[][] EMPTY_2D_BYTE_ARRAY = new byte[0][];

    private final RedisSerializer<String> serializer = RedisSerializer.string();

    public JpowerRedisConnection(RedisConnection connection) {
        super(connection);
    }

    @Override
    public Long del(String... keys) {
        return del(serializeMulti(keys));
    }

    private byte[][] serializeMulti(String... keys) {

        if (keys == null) {
            return EMPTY_2D_BYTE_ARRAY;
        }

        byte[][] ret = new byte[keys.length][];

        for (int i = 0; i < ret.length; i++) {
            ret[i] = serializer.serialize(keys[i]);
        }

        return ret;
    }
}
