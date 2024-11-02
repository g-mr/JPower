package top.jpower.core.redis.topic;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.RedisSerializer;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.ObjectUtil;

import java.lang.reflect.Type;

/**
 * Redis Pub/Sub 消息处理器
 *
 * @author mr.g
 * @date 2024-11-1 22:29
 */
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Slf4j
public class RedisTopicListenerAdapter implements MessageListener {

    private final RedisTopicListener<?> redisTopicListener;
    private final RedisTopic redisTopic;
    private RedisSerializer<String> keySerializer = RedisSerializer.string();
    private RedisSerializer<?> valueSerializer = RedisSerializer.string();


    @Override
    public void onMessage(Message message, byte[] pattern) {
        String channel = getKeySerializer().deserialize(message.getChannel());
        try {
            if (redisTopic.isJsonSource()) {
                try {
                    redisTopicListener.handleMessage(channel, JSON.parseObject(message.getBody(), parseParamClass()));
                } catch (Exception e){
                    Object value = getValueSerializer().deserialize(message.getBody());
                    redisTopicListener.handleMessage(channel, JSON.parseObject(Fc.toStr(value), parseParamClass()));
                }
            } else {
                Object value = getValueSerializer().deserialize(message.getBody());
                redisTopicListener.handleMessage(channel, Convert.convert(parseParamClass(), value));
            }
        } catch (Exception exception) {
            log.warn("无法进行类型转换，请检查{}接受值,error={}", redisTopicListener.getClass().getName(), exception.getMessage());
            throw exception;
        }
    }

    private Type parseParamClass() {
        return TypeUtil.getTypeArgument(redisTopicListener.getClass());
    }

    public RedisSerializer<String> getKeySerializer(){
        return ObjectUtil.defaultIfNull(redisTopicListener.channelSerializer(), keySerializer);
    }

    public RedisSerializer<?> getValueSerializer(){
        return ObjectUtil.defaultIfNull(redisTopicListener.messageSerializer(), valueSerializer);
    }

}
