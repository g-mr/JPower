package top.jpower.core.redis.connection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author mr.g
 * @date 2024-9-10 21:45
 * @description
 */
@AllArgsConstructor
@Getter
public class RedisConnectionFactoryManage {

    private RedisConnectionFactory factory;

}
