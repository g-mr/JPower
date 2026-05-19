package top.jpower.core.asterisk.ami;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.Predicate;

@Component
@Slf4j
public class AmiEventEmitter {

    // onBackpressureBuffer(): 当订阅者处理慢时，在内存中缓冲事件。
    private final Sinks.Many<ManagerEvent> eventSink = Sinks.many().multicast().onBackpressureBuffer();

    // 暴露给外部的Flux，用于订阅事件流
    private final Flux<ManagerEvent> eventFlux = eventSink.asFlux().share();

    /**
     * 发射AMI事件到响应式流。
     * 使用 tryEmitNext，其返回值（Sinks.EmitResult）指示了发射状态（成功、失败等）。
     */
    public void emit(ManagerEvent event) {
        Sinks.EmitResult result = eventSink.tryEmitNext(event);
        if (result.isFailure()) {
            // 根据失败原因进行处理，例如重试或记录日志
            // 常见失败原因：Sinks.EmitResult.FAIL_NON_SERIALIZED, FAIL_OVERFLOW, FAIL_CANCELLED, FAIL_TERMINATED
            log.error("Failed to emit AMI event: {}, reason: {}", event, result);
        }
    }

    /**
     * 订阅所有事件。
     */
    public Flux<ManagerEvent> on() {
        return eventFlux;
    }

    /**
     * 订阅特定类型的事件。
     */
    public <T extends ManagerEvent> Flux<T> on(Class<T> eventType) {
        return eventFlux
                .filter(eventType::isInstance)
                .cast(eventType);
    }

    /**
     * 订阅特定类型的事件后进行自定义过滤。
     */
    public <T extends ManagerEvent> Flux<T> on(Class<T> eventType, Predicate<T> filter) {
        return eventFlux
                .filter(eventType::isInstance)
                .cast(eventType)
                .filter(filter)
                .publish()
                .autoConnect();
    }

    /**
     * 安全地关闭事件流（发出完成信号）。
     */
    @PreDestroy
    public void complete() {
        eventSink.tryEmitComplete();
    }
}