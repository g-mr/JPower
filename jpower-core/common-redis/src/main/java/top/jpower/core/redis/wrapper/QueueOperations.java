package top.jpower.core.redis.wrapper;

import cn.hutool.core.convert.Convert;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.connection.RedisPubSubCommands;
import org.springframework.data.redis.connection.Subscription;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.Assert;

import java.util.function.BiConsumer;

/**
 * @author mr.g
 * @date 2024-10-16 21:38
 * @description
 */
@AllArgsConstructor
public class QueueOperations<T> {

    private final RedisTemplate<String, Object> redisTemplate;
    private final Class<T> clz;

    /**
     * Indicates whether the current connection is subscribed (to at least one channel) or not.
     *
     * @return true if the connection is subscribed, false otherwise
     */
    @SuppressWarnings("ConstantConditions")
    public boolean isSubscribed() {
        return redisTemplate.execute(RedisPubSubCommands::isSubscribed, true);
    }

    /**
     * Returns the current subscription for this connection or null if the connection is not subscribed.
     *
     * @return the current subscription, {@literal null} if none is available.
     */
    public Subscription getSubscription() {
        return redisTemplate.execute(RedisPubSubCommands::getSubscription, true);
    }

    /**
     * Publishes the given message to the given channel.
     *
     * @param channel the channel to publish to. Must not be {@literal null}.
     * @param message message to publish. Must not be {@literal null}.
     * @return the number of clients that received the message or {@literal null} when used in pipeline / transaction.
     * @see <a href="https://redis.io/commands/publish">Redis Documentation: PUBLISH</a>
     */
    public Long publish(String channel, T message) {
        Assert.hasText(channel, "a non-empty channel is required");

        byte[] rawChannel = rawString(channel);
        byte[] rawMessage = rawValue(message);

        return redisTemplate.execute(connection -> {
            return connection.publish(rawChannel, rawMessage);
        }, true);
    }

    /**
     * Subscribes the connection to the given channels. Once subscribed, a connection enters listening mode and can only
     * subscribe to other channels or unsubscribe. No other commands are accepted until the connection is unsubscribed.
     * <p>
     * Note that this operation is blocking and the current thread starts waiting for new messages immediately.
     *
     * @param listener message listener, must not be {@literal null}.
     * @param channels channel names, must not be {@literal null}.
     * @see <a href="https://redis.io/commands/subscribe">Redis Documentation: SUBSCRIBE</a>
     */
    public void subscribe(BiConsumer<String, T> listener, String... channels) {

        byte[][] rawMessage = rawStrings(channels);

        redisTemplate.execute(connection->{
            connection.subscribe((message, channel)->
                    listener.accept(redisTemplate.getStringSerializer().deserialize(message.getChannel()), Convert.convert(clz, redisTemplate.getValueSerializer().deserialize(message.getBody())))
                , rawMessage);
            return null;
        }, true);
    }

    /**
     * Subscribes the connection to all channels matching the given patterns. Once subscribed, a connection enters
     * listening mode and can only subscribe to other channels or unsubscribe. No other commands are accepted until the
     * connection is unsubscribed.
     * <p>
     * Note that this operation is blocking and the current thread starts waiting for new messages immediately.
     *
     * @param listener message listener, must not be {@literal null}.
     * @param patterns channel name patterns, must not be {@literal null}.
     * @see <a href="https://redis.io/commands/psubscribe">Redis Documentation: PSUBSCRIBE</a>
     */
    public void pSubscribe(BiConsumer<String, T> listener, String... patterns) {
        byte[][] rawPatterns = rawStrings(patterns);

        redisTemplate.execute(connection->{
            connection.pSubscribe((message, channel)->
                    listener.accept(redisTemplate.getStringSerializer().deserialize(message.getChannel()), Convert.convert(clz, redisTemplate.getValueSerializer().deserialize(message.getBody())))
                , rawPatterns);
            return null;
        }, true);
    }


    private byte[][] rawStrings(String... keys) {
        final byte[][] rawKeys = new byte[keys.length][];

        int i = 0;
        for (String key : keys) {
            rawKeys[i++] = rawString(key);
        }

        return rawKeys;
    }

    private byte[] rawString(String str) {
        Assert.notNull(str, "non null channel required");
        return redisTemplate.getStringSerializer().serialize(str);
    }

    @SuppressWarnings("unchecked")
    private byte[] rawValue(Object value) {
        //noinspection rawtypes
        RedisSerializer redisSerializer = redisTemplate.getValueSerializer();
        return redisSerializer.serialize(value);
    }
}
