package top.jpower.core.exception.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.feign.LogClient;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.exception.utils.FieldCompletionUtil;

/**
 * 异步监听操作日志
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 1:01
 */
@RequiredArgsConstructor
public class OperateLogListener {

    private final JpowerProperties properties;

    @Async
    @EventListener(OperateLogEvent.class)
    public void saveApiLog(OperateLogEvent event) {
        OperateLogDto operateLog = (OperateLogDto) event.getSource();
        FieldCompletionUtil.serverInfo(operateLog,properties);
        LogClient.getInstance(properties.getServer()).saveOperateLog(operateLog);
    }

}
