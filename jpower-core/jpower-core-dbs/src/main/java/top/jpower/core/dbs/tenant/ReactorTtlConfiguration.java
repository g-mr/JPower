package top.jpower.core.dbs.tenant;

import com.alibaba.ttl.TtlRunnable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import reactor.core.scheduler.Schedulers;

/**
 * Reactor调度器TTL上下文传播配置<br/>
 * 通过注册{@link Schedulers#onScheduleHook}钩子，使得Reactor调度器在调度任务时
 * 自动将{@link Runnable}包装为{@link TtlRunnable}，确保{@link com.alibaba.ttl.TransmittableThreadLocal}
 * 中的值在线程切换时能够正确传播。
 *
 * @author mr.g
 */
@Slf4j
@AutoConfiguration
@ConditionalOnClass({Schedulers.class, TtlRunnable.class})
public class ReactorTtlConfiguration {

    private static final String TTL_HOOK_KEY = "ttl-context-propagation";

    public ReactorTtlConfiguration() {
        Schedulers.onScheduleHook(TTL_HOOK_KEY, runnable -> {
            TtlRunnable ttlRunnable = TtlRunnable.get(runnable, false, true);
            return ttlRunnable != null ? ttlRunnable : runnable;
        });
        log.info("已注册Reactor调度器TTL上下文传播钩子");
    }

}
