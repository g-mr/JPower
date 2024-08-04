package top.jpower.core.exception.listener;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.feign.LogClient;
import top.jpower.core.exception.model.ErrorLogDto;
import top.jpower.core.exception.utils.FieldCompletionUtil;

/**
 * 异步监听操作日志
 *
 * @author mr.g
 * @date 2021/5/1 0001 1:01
 */
@RequiredArgsConstructor
public class ErrorLogListener {

    private final JpowerProperties properties;

    @Async
    @EventListener(ErrorLogEvent.class)
    public void saveApiLog(ErrorLogEvent event) {
        ErrorLogDto errorLog = (ErrorLogDto) event.getSource();
        FieldCompletionUtil.serverInfo(errorLog,properties);
        LogClient.getInstance(properties.getServer()).saveErrorLog(errorLog);
    }

}
