package top.jpower.jpower.module.base.listener;

import top.jpower.jpower.module.base.feign.LogClient;
import top.jpower.jpower.module.base.model.ErrorLogDto;
import top.jpower.jpower.module.base.utils.FieldCompletionUtil;
import top.jpower.jpower.module.common.deploy.props.JpowerProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * 异步监听操作日志
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 1:01
 */
@Slf4j
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
