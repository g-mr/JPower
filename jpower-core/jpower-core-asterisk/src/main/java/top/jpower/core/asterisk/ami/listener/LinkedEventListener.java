package top.jpower.core.asterisk.ami.listener;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.manager.event.ManagerEvent;
import top.jpower.core.util.utils.Fc;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

/**
 * 对事件进行链路管理
 *
 * @author mr.g
 */
@Slf4j
public class LinkedEventListener extends EventAbstractListener {

    /**
     * 链路事件缓存
     */
    protected final static TimedCache<String, LinkedList<ManagerEvent>> CACHE = CacheUtil.newTimedCache(1000 * 60 * 60, 1000 * 60);

    /**
     * 监听器总数，需在启动时初始化
     */
    private static final ConcurrentMap<String, AtomicInteger> LISTENER_COUNT = new ConcurrentHashMap<>();

    /**
     * 事件计数器映射：eventKey -> 剩余待处理实例数
     */
    private static final ConcurrentHashMap<String, AtomicInteger> COUNTERS = new ConcurrentHashMap<>();

    /**
     * 监听器总数，需在启动时初始化
     */
    public static ConcurrentMap<String, AtomicInteger> LISTENER_COUNT() {
        return LISTENER_COUNT;
    }

    /**
     * 链路事件缓存
     */
    @Override
    public void onManagerEvent(ManagerEvent event) {
        try {
            // 先执行
            super.onManagerEvent(event);
        } finally {
            // 后缓存
            afterEventProcessed(event);
        }
    }

    /**
     * 获取链路事件最后一个满足得事件
     *
     * @param linkedId 链路ID
     * @return 索引
     */
    protected int lastIndexOf(String linkedId, Predicate<ManagerEvent> pred) {
        List<ManagerEvent> list = CACHE.get(linkedId);
        ListIterator<ManagerEvent> it = list.listIterator(list.size());
        while (it.hasPrevious()) {
            if (pred.test(it.previous())) {
                return it.nextIndex();
            }
        }
        return -1;
    }

    private void afterEventProcessed(ManagerEvent event) {
        Map<String, Object> map = BeanUtil.beanToMap(event);
        if (!map.containsKey("linkedid") && !map.containsKey("linkedId")) {
            return;
        }
        String linkedId = Fc.toStr(map.getOrDefault("linkedid", map.get("linkedId")));
        if (Fc.isBlank(linkedId)) {
            return;
        }

        String eventKey = linkedId + "_" + event.getClass().getName() + "_" + System.identityHashCode(event);

        AtomicInteger counter = COUNTERS.computeIfAbsent(eventKey, k -> {
            if (LISTENER_COUNT.get(event.getClass().getName()).get() <= 0) {
                log.warn("LISTENER_COUNT未设定，假设1.这可能导致缓存错误。");
                return new AtomicInteger(1);
            }
            return new AtomicInteger(LISTENER_COUNT.get(event.getClass().getName()).get());
        });

        int remaining = counter.decrementAndGet();
        if (remaining <= 0) {
            synchronized (CACHE) {
                LinkedList<ManagerEvent> list = CACHE.get(linkedId, LinkedList::new);
                if (!list.contains(event)) {
                    list.add(event);
                }
            }
            COUNTERS.remove(eventKey);
            log.debug("为 linkedID 缓存事件：{}", linkedId);
        } else {
            log.debug("事件由一名监听器处理，剩余： {}", remaining);
        }
    }

}
