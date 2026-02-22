package top.jpower.core.exception.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.client.LogClient;
import top.jpower.core.exception.model.ErrorLogDTO;
import top.jpower.core.exception.utils.FieldCompletionUtil;
import top.jpower.core.util.utils.Fc;

/**
 * 异步监听操作日志
 *
 * @author mr.g
 * @date 2021/5/1 0001 1:01
 */
@Slf4j
@RequiredArgsConstructor
public class ErrorLogListener {

    private final JpowerProperties properties;
    private final LogClient logClient;

    @Async
    @EventListener(ErrorLogEvent.class)
    public void saveApiLog(ErrorLogEvent event) {
        ErrorLogDTO errorLog = (ErrorLogDTO) event.getSource();
        FieldCompletionUtil.serverInfo(errorLog,properties);
        if (Fc.notNull(logClient)){
            logClient.saveErrorLog(errorLog);
        } else {
            log.warn("未发现LogClient实现类，无法处理错误日志，请继承LogClient进行实现...");
        }
    }

}
