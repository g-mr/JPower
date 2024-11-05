package top.jpower.core.redis.handler;

import org.redisson.api.NameMapper;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import top.jpower.core.redis.connection.JpowerRedisConnection;
import top.jpower.core.redis.handler.PrefixRedissonHandler;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;
import top.jpower.core.redis.serializer.JpowerStringSerializer;

/**
 * @author mr.g
 * @date 2024-10-27 23:04
 * @description
 */
public class JpowerStringRedisTemplate extends StringRedisTemplate {

    private final RedisProperties redisProperties;
    private final RedisPrefixHandler redisPrefixHandler;
    private final NameMapper nameMapper;

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
     * and {@link #afterPropertiesSet()} still need to be called.
     */
    public JpowerStringRedisTemplate(RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler) {
        setKeySerializer(new JpowerStringSerializer());
        setHashKeySerializer(new JpowerStringSerializer());
        this.redisProperties = redisProperties;
        this.redisPrefixHandler = redisPrefixHandler;
        nameMapper = new PrefixRedissonHandler(redisProperties.getPrefix(), redisPrefixHandler);
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance. {@link #setConnectionFactory(RedisConnectionFactory)}
     * and {@link #afterPropertiesSet()} still need to be called.
     */
    public JpowerStringRedisTemplate(RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler, NameMapper nameMapper) {
        setKeySerializer(new JpowerStringSerializer());
        setHashKeySerializer(new JpowerStringSerializer());
        this.redisProperties = redisProperties;
        this.redisPrefixHandler = redisPrefixHandler;
        this.nameMapper = nameMapper;
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
     *
     * @param connectionFactory connection factory for creating new connections
     */
    public JpowerStringRedisTemplate(RedisConnectionFactory connectionFactory, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler) {
        this(redisProperties, redisPrefixHandler);
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    /**
     * Constructs a new <code>StringRedisTemplate</code> instance ready to be used.
     *
     * @param connectionFactory connection factory for creating new connections
     */
    public JpowerStringRedisTemplate(RedisConnectionFactory connectionFactory, RedisProperties redisProperties, RedisPrefixHandler redisPrefixHandler, NameMapper nameMapper) {
        this(redisProperties, redisPrefixHandler, nameMapper);
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

    protected RedisConnection preProcessConnection(RedisConnection connection, boolean existingConnection) {
        return new JpowerRedisConnection(connection, redisProperties.getPrefix(), redisPrefixHandler, nameMapper, getKeySerializer());
    }

}
