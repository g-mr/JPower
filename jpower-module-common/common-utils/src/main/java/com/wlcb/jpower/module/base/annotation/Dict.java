package com.wlcb.jpower.module.base.annotation;


import com.wlcb.jpower.module.common.utils.constants.StringPool;

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
    String name();

    /**
     * 赋值的属性名称
     */
    String attributes() default StringPool.EMPTY;;

}
