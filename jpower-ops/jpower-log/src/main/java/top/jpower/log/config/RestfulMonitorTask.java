package top.jpower.log.config;

import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;
import top.jpower.log.properties.MonitorRestfulProperties;
import top.jpower.log.service.TaskService;

/**
 * @Description TODO 接口监控定时任务执行器
 * @Author mr.g
 * @Date 2021/4/1 0001 22:26
 */
@Component
@EnableScheduling
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
                            return new CronTrigger(cron).nextExecutionTime(triggerContext).toInstant();
                        })
            );
        }
    }

}
