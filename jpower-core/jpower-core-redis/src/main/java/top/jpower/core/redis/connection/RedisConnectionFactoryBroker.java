package top.jpower.core.redis.connection;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.redis.handler.PrefixRedissonHandler;
import top.jpower.core.redis.handler.RedisPrefixHandler;
import top.jpower.core.redis.properties.RedisProperties;

/**
 * @author mr.g
 * @date 2024-11-1 22:46
 * @description
 */
@RequiredArgsConstructor
public class RedisConnectionFactoryBroker implements RedisConnectionFactory {

    private final RedisConnectionFactory redisConnectionFactory;
    private final RedisProperties redisProperties;
    private final RedisPrefixHandler redisPrefixHandler;

    @Override
    public RedisConnection getConnection() {
        return new JpowerRedisConnection(redisConnectionFactory.getConnection(), redisProperties.getPrefix(), redisPrefixHandler, new PrefixRedissonHandler(redisProperties.getPrefix(), redisPrefixHandler), RedisSerializer.string());
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return redisConnectionFactory.getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return redisConnectionFactory.getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return redisConnectionFactory.getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return redisConnectionFactory.translateExceptionIfPossible(ex);
    }

}
