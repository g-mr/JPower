package top.jpower.core.redis.serializer;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author mr.g
 * @date 2024-10-27 19:58
 * @description
 */
public class JpowerStringSerializer implements RedisSerializer<Object> {

    private final Charset charset;

    /**
     * Creates a new {@link StringRedisSerializer} using {@link StandardCharsets#UTF_8 UTF-8}.
     */
    public JpowerStringSerializer() {
        this(StandardCharsets.UTF_8);
    }

    /**
     * Creates a new {@link StringRedisSerializer} using the given {@link Charset} to encode and decode strings.
     *
     * @param charset must not be {@literal null}.
     */
    public JpowerStringSerializer(Charset charset) {

        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#deserialize(byte[])
     */
    @Override
    public String deserialize(@Nullable byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.redis.serializer.RedisSerializer#serialize(java.lang.Object)
     */
    @Override
    public byte[] serialize(@Nullable Object string) {
        return (string == null ? null : string.toString().getBytes(charset));
    }

    @Override
    public Class<?> getTargetType() {
        return String.class;
    }

}
