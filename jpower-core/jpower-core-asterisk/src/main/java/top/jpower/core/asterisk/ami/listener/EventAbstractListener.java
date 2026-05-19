package top.jpower.core.asterisk.ami.listener;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.ClassUtil;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.ManagerEventListener;
import org.asteriskjava.manager.event.ManagerEvent;
import top.jpower.core.asterisk.ami.annotation.AmiEvent;
import top.jpower.core.util.utils.Fc;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 实现 ManagerEventListener 接口，对事件进行不同方法的调用
 *
 * @author mr.g
 */
@Slf4j
public abstract class EventAbstractListener implements ManagerEventListener {

    private static final ConcurrentMap<Class<?>, Map<Class<?>, List<Method>>> CLASS_HANDLER_CACHE = new ConcurrentHashMap<>();

    /**
     * 监听事件执行不同方法
     *
     * @param event 事件
     */
    public void onManagerEvent(ManagerEvent event) {
        // 获取当前实例对应类的处理器映射
        Map<Class<?>, List<Method>> handlerMap = CLASS_HANDLER_CACHE.computeIfAbsent(
                this.getClass(),
                this::buildHandlerMap
        );

        List<Method> methods = handlerMap.get(event.getClass());
        if (Fc.isNotEmpty(methods)) {
            methods.forEach(method -> invokeMethod(method, event));
        }
    }

    private Map<Class<?>, List<Method>> buildHandlerMap(Class<?> clazz) {
        List<Method> allMethods = ClassUtil.getPublicMethods(clazz,
                method -> AnnotationUtil.hasAnnotation(method, AmiEvent.class)
        );
        Map<Class<?>, List<Method>> map = new HashMap<>();
        for (Method method : allMethods) {
            AmiEvent annotation = method.getAnnotation(AmiEvent.class);
            Class<?> eventType = annotation.value();
            map.computeIfAbsent(eventType, k -> new ArrayList<>()).add(method);
        }
        return map;
    }

    private void invokeMethod(Method method, ManagerEvent event) {
        try {
            method.invoke(this, event);
        } catch (Exception e) {
            log.error("AMI事件处理异常=====>>", e);
        }
    }

}
