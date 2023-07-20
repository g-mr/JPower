package com.wlcb.jpower.config;

import com.wlcb.jpower.task.DelayTask;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.DelayQueue;

/**
 * @author mr.g
 * @date 2023/6/26 11:49 PM
 */
@Configuration(proxyBeanMethods = false)
public class DelayQueueConfig {

    @Bean
    public DelayQueue<DelayTask> delayQueue(){
        return new DelayQueue<>();
    }

}
