package com.wlcb.jpower.module.base.listener;

import com.wlcb.jpower.module.base.feign.LogClient;
import com.wlcb.jpower.module.base.model.OperateLogDto;
import com.wlcb.jpower.module.base.utils.FieldCompletionUtil;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
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

    private final LogClient logService;
    private final JpowerProperties properties;

    @Async
    @EventListener(OperateLogEvent.class)
    public void saveApiLog(OperateLogEvent event) {
        OperateLogDto operateLog = (OperateLogDto) event.getSource();
        FieldCompletionUtil.serverInfo(operateLog,properties);
        logService.saveOperateLog(operateLog);
    }

}
