package top.jpower.jpower.module.base.listener;

import org.springframework.context.ApplicationEvent;
import top.jpower.jpower.module.base.model.OperateLogDto;

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
    public OperateLogEvent(OperateLogDto log) {
        super(log);
    }
}
