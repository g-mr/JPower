package top.jpower.jpower.annotation;

import java.lang.annotation.*;

/**
 * 延时任务
 *
 * @author mr.gmac
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JpowerDelayTask {

    /**
     * 任务名称
     **/
    String name();

    /**
     * 失败重试次数
     **/
    int retry() default 0;

}
