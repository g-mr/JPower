package top.jpower.core.redis.topic;

import lombok.Data;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.util.Assert.notEmpty;

/**
 * @author mr.g
 * @date 2024-10-31 23:42
 * @description
 */
@Data
public class RedisTopicScannerConfigurer implements InitializingBean, BeanNameAware {

    private String beanName;

    private Set<String> basePackages = new HashSet<>();

    @Override
    public void afterPropertiesSet() {
        notEmpty(this.basePackages, "Property 'basePackages' is required");
    }
}
