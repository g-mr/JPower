package com.wlcb.jpower.module.base.annotation;


import java.lang.annotation.*;


/**
 * @Author 郭丁志
 * @Description //TODO 自定义字典查询
 * @Date 11:16 2020-07-17
 **/
@Target({ ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Dict {

    /**
     * 字典名称
     */
    String name();

    /**
     * 赋值的属性名称
     * @Desc 在 {@link IDictBindHandler#setMetaObject(Dict, Object, MetaObject)}中的dict参数
     */
    String attributes() default "";


}
