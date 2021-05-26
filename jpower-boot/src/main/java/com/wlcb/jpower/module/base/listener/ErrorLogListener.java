package com.wlcb.jpower.module.base.listener;

import com.wlcb.jpower.module.base.model.ErrorLogDto;
import com.wlcb.jpower.module.base.utils.FieldCompletionUtil;
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.service.ErrorLogService;
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

    private final ErrorLogService logService;
    private final JpowerProperties properties;

    @Async
    @EventListener(ErrorLogEvent.class)
    public void saveApiLog(ErrorLogEvent event) {
        ErrorLogDto errorLog = (ErrorLogDto) event.getSource();
        FieldCompletionUtil.serverInfo(errorLog,properties);
        logService.saveErrorLog(errorLog);
    }

}
