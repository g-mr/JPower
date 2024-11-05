package top.jpower.core.redis.lock;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 分布式锁
 *
 * @author mr.g
 **/
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GlobalLock {

    /**
     * 锁名称
     **/
    @AliasFor("name")
    String value();

    /**
     * 锁名称
     **/
    @AliasFor("value")
    String name();

    /**
     * 是否公平锁，否则是重入锁
     **/
    boolean isFair() default true;

    /**
     * 获取锁的最大等待时间
     * <br/>
     * 默认10秒
     **/
    long waitTime() default 10;

    /**
     * 锁离开时间
     * <br/>
     * 默认50秒
     **/
    long leaveTime() default 50;

    /**
     * 时间单位
     **/
    TimeUnit unit() default TimeUnit.SECONDS;

}
