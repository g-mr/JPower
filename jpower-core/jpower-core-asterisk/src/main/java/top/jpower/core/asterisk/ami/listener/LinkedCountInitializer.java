package top.jpower.core.asterisk.ami.listener;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import lombok.RequiredArgsConstructor;
import org.asteriskjava.manager.event.ManagerEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import top.jpower.core.asterisk.ami.annotation.AmiEvent;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.util.utils.Fc;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class LinkedCountInitializer {

    private final ApplicationContext applicationContext;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        Map<String, LinkedEventListener> listeners = applicationContext.getBeansOfType(LinkedEventListener.class);
        listeners.forEach((name, listener) -> {
            AmiListener amiListener = AnnotationUtil.getAnnotation(listener.getClass(), AmiListener.class);
            if (Fc.isNotEmpty(amiListener.value())) {
                for (Class<? extends ManagerEvent> clz : amiListener.value()) {
                    LinkedEventListener.LISTENER_COUNT().computeIfAbsent(clz.getName(), k -> new AtomicInteger(0)).incrementAndGet();
                }
            } else {
                List<Method> allMethods = ClassUtil.getPublicMethods(listener.getClass(),
                        method -> AnnotationUtil.hasAnnotation(method, AmiEvent.class)
                );
                if (Fc.isNotEmpty(allMethods)) {
                    allMethods.forEach(method -> {
                        AmiEvent annotation = method.getAnnotation(AmiEvent.class);
                        LinkedEventListener.LISTENER_COUNT().computeIfAbsent(annotation.value().getName(), k -> new AtomicInteger(0)).incrementAndGet();
                    });
                }
            }

        });
    }

}
