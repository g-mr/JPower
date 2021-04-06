package com.wlcb.jpower.config;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.properties.MonitorRestfulProperties;
import com.wlcb.jpower.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

/**
 * @Description TODO 接口监控定时任务执行器
 * @Author mr.g
 * @Date 2021/4/1 0001 22:26
 */
@Configuration
@EnableScheduling
@EnableConfigurationProperties(MonitorRestfulProperties.class)
@AllArgsConstructor
public class RestfulMonitorTask implements SchedulingConfigurer {

    private final MonitorRestfulProperties properties;
    private final TaskService taskService;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        if (properties.getEnable()){
            properties.getRoutes().forEach(route ->
                scheduledTaskRegistrar.addTriggerTask(() -> taskService.process(route),
                        triggerContext -> {
                            String cron = properties.getCron();
                            if (Fc.isBlank(cron)) {
                                throw new RuntimeException("restfulMonitorTask cron is null");
                            }
                            return new CronTrigger(cron).nextExecutionTime(triggerContext);
                        })
            );
        }
    }


}
