package top.jpower.core.exception.listener;

import org.springframework.context.ApplicationEvent;
import top.jpower.core.exception.model.OperateLogDTO;

/**
 * 异步监听操作日志
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 1:01
 */
public class OperateLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = -3753172113891421993L;

    /**
     * Create a new {@code ApplicationEvent}.
     */
    public OperateLogEvent(OperateLogDTO log) {
        super(log);
    }
}
