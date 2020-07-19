package com.wlcb.jpower.module.base.annotation;


import java.lang.annotation.*;


/**
 * @Author 郭丁志
 * @Description //TODO 自定义字典查询
 * @Date 11:16 2020-07-17
 **/
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Dict {

    /**
     * 字典名称
     */
    public String name();

    /**
     * 赋值的属性名称
     */
    public String attributes() default "";

}
