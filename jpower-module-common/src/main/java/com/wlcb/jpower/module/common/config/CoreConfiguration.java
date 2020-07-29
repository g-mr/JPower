package com.wlcb.jpower.module.common.config;

import com.wlcb.jpower.module.common.utils.SpringUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * @ClassName ToolConfiguration
 * @Description TODO 工具配置类
 * @Author 郭丁志
 * @Date 2020-07-28 22:59
 * @Version 1.0
 */
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CoreConfiguration {

    /**
     * Spring上下文缓存
     *
     * @return SpringUtil
     */
    @Bean
    public SpringUtil springUtil() {
        return new SpringUtil();
    }

}
