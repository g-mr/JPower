package top.jpower.jpower.module.base.listener;

import top.jpower.jpower.module.base.feign.LogClient;
import top.jpower.jpower.module.base.model.OperateLogDto;
import top.jpower.jpower.module.base.utils.FieldCompletionUtil;
import top.jpower.jpower.module.common.deploy.props.JpowerProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

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
