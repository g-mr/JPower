package top.jpower.jpower.test;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

/**
 * @author mr.g
 * @date 2024-10-17 22:50
 * @description
 */
@Component
@RequiredArgsConstructor
public class MySubcribe implements MessageListener {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(Message message, byte[] bytes) {
        RedisSerializer serializer = redisTemplate.getValueSerializer();
        String deserialize = (String) serializer.deserialize(message.getBody());
        System.out.println("接收数据:"+deserialize);
        System.out.println("订阅频道:"+new String(message.getChannel()));
    }
}