package com.qidiangk.smart.maxkb.config.annotation;

import top.jpower.core.util.rsp.R;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Return {

    /**
     * 包装类型
     */
    Class<?> clz() default R.class;

    /**
     * code字段正确的名称
     */
    int[] successCode() default 200;

    /**
     * code字段名称
     */
    String code() default "";

    /**
     * message字段名称
     */
    String message() default "";

    /**
     * data字段名称
     */
    String data() default "data";

}
