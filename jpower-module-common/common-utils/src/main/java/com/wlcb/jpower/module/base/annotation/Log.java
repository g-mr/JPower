package com.wlcb.jpower.module.base.annotation;


import com.wlcb.jpower.module.base.enums.BusinessType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author mr.gmac
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log{

    /** 模块 **/
    @AliasFor("title")
    String value() default "";

    /** 模块 **/
    @AliasFor("value")
    String title() default "";

    /** 功能类型 **/
    BusinessType businessType() default BusinessType.OTHER;

    /** 是否需要记录到数据库 (该配置暂时无效，功能待实现) **/
    boolean isSaveLog() default false;

    /** 是否保存请求的参数 **/
    boolean isSaveRequestData() default true;
}
