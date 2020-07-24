package com.wlcb.jpower.module.common.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Thread.UncaughtExceptionHandler;

/**
 * @ClassName NamedThreadFactory
 * @Description TODO 自定义线程名字
 * @Author 郭丁志
 * @Date 2020-06-28 17:52
 * @Version 1.0
 */
public class NamedThreadFactory implements ThreadFactory {
    private static final Logger logger = LoggerFactory.getLogger(NamedThreadFactory.class);
    private final ThreadGroup group;
    private final AtomicInteger counter;
    private final String namePrefix;
    private static Map<String, AtomicInteger> map = new ConcurrentHashMap();

    public NamedThreadFactory(String name) {
        this.counter = getCounter(name);
        this.group = new ThreadGroup(name);
        this.namePrefix = name;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.namePrefix);
        buffer.append('-');
        buffer.append(this.counter.getAndIncrement());
        Thread t = new Thread(this.group, runnable, buffer.toString(), 0L);
        t.setDaemon(false);
        t.setPriority(5);
        return t;
    }

    private static AtomicInteger getCounter(String name) {
        AtomicInteger counter = (AtomicInteger)map.get(name);
        if (counter == null) {
            synchronized(map) {
                counter = (AtomicInteger)map.get(name);
                if (counter == null) {
                    counter = new AtomicInteger();
                    map.put(name, counter);
                }
            }
        }

        return counter;
    }

    public static class UncaughtException extends RuntimeException {
        private static final long serialVersionUID = 1L;
        private String data;

        public UncaughtException() {
        }

        public UncaughtException(String message, Throwable cause, String data) {
            super(message, cause);
            this.data = data;
        }

        public String getData() {
            return this.data;
        }
    }

    private static class DefaultUncaughtExceptionHandler implements UncaughtExceptionHandler {
        private String name;

        public DefaultUncaughtExceptionHandler() {
            this.name = "";
        }

        public DefaultUncaughtExceptionHandler(String name) {
            this.name = name;
        }

        @Override
        public void uncaughtException(Thread t, Throwable e) {
            if (e instanceof NamedThreadFactory.UncaughtException) {
                NamedThreadFactory.logger.error("e.clz={}, e.msg={}, data={}", new Object[]{e.getCause().getClass().getSimpleName(), e.getMessage(), ((NamedThreadFactory.UncaughtException)e).getData()});
            } else {
                NamedThreadFactory.logger.error("e.clz={}, e.msg={}, e.stack={}", new Object[]{e.getCause().getClass().getSimpleName(), e.getMessage(), e.getStackTrace()});
            }

        }
    }
}
