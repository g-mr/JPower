package top.jpower.core.exception.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.client.LogClient;
import top.jpower.core.exception.model.OperateLogDto;
import top.jpower.core.exception.utils.FieldCompletionUtil;
import top.jpower.core.util.utils.Fc;

/**
 * 异步监听操作日志
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 1:01
 */
@Slf4j
@RequiredArgsConstructor
public class OperateLogListener {

    private final JpowerProperties properties;
    private final LogClient logClient;

    @Async
    @EventListener(OperateLogEvent.class)
    public void saveApiLog(OperateLogEvent event) {
        OperateLogDto operateLog = (OperateLogDto) event.getSource();
        FieldCompletionUtil.serverInfo(operateLog,properties);
        if (Fc.notNull(logClient)){
            logClient.saveOperateLog(operateLog);
        } else {
            log.warn("未发现LogClient实现类，无法处理操作日志，请继承LogClient进行实现...");
        }
    }

}
