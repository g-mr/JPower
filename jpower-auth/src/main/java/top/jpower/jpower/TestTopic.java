package top.jpower.jpower;

import top.jpower.core.redis.topic.RedisTopic;
import top.jpower.core.redis.topic.RedisTopicListener;

/**
 * @author mr.g
 * @date 2024-11-5 23:13
 * @description
 */
@RedisTopic("test")
public class TestTopic implements RedisTopicListener<String> {
    @Override
    public void handleMessage(String topic, String message) {
        System.out.println(topic+"---------"+message);
    }
}
