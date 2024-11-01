package top.jpower.core.redis.topic;

import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * Redis Pub/Sub 消息监听
 *
 * @author mr.g
 * @date 2024-11-1 21:45
 */
public interface RedisTopicListener<T> {

    /**
     * 消息接受
     *
     * @author mr.g
     * @param topic 通道名称
     * @param message 消息
     **/
    void handleMessage(String topic, T message);

    /**
     * 自定义channel序列化
     *
     * @author mr.g
     * @param
     * @return org.springframework.data.redis.serializer.RedisSerializer<java.lang.String>
     **/
    default RedisSerializer<String> channelSerializer(){
        return null;
    }

    /**
     * 自定义消息序列化
     *
     * @author mr.g
     * @param
     * @return org.springframework.data.redis.serializer.RedisSerializer<T>
     **/
    default RedisSerializer<T> messageSerializer(){
        return null;
    }

}
