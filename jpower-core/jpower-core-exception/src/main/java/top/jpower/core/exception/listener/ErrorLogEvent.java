package top.jpower.core.exception.listener;

import org.springframework.context.ApplicationEvent;
import top.jpower.core.exception.model.ErrorLogDTO;

/**
 * 异步监听操作日志
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 1:01
 */
public class ErrorLogEvent extends ApplicationEvent {

    private static final long serialVersionUID = 2989466062036390477L;

    /**
     * Create a new {@code ApplicationEvent}.
     */
    public ErrorLogEvent(ErrorLogDTO log) {
        super(log);
    }
}
