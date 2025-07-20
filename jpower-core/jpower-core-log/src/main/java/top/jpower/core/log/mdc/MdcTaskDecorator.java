package top.jpower.core.log.mdc;

import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * MDC线程之间传递装饰器
 *
 * @author mr.g
 * @date 2025-7-20 20:53
 */
public class MdcTaskDecorator implements TaskDecorator {

    @Override
    public Runnable decorate(Runnable runnable) {
        // 1. 捕获当前线程的MDC上下文
        Map<String, String> contextMap = MDC.getCopyOfContextMap();

        return () -> {
            try {
                // 2. 将MDC上下文注入到新线程
                if (contextMap != null) {
                    MDC.setContextMap(contextMap);
                }
                // 3. 执行原始任务
                runnable.run();
            } finally {
                // 4. 清理新线程的MDC上下文
                MDC.clear();
            }
        };
    }
}