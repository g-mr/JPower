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
     * <br/>
     * <per>
     *     e.g: 支持EL表达式
     * </per>
     **/
    @AliasFor("name")
    String value();

    /**
     * 锁名称
     * <br/>
     * <per>
     *     e.g: 支持EL表达式
     * </per>
     **/
    @AliasFor("value")
    String name();

    /**
     * 锁类型
     **/
    LockTypeEnum type() default LockTypeEnum.FAIR;

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
