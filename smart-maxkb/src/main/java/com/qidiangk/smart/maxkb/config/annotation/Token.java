package com.qidiangk.smart.maxkb.config.annotation;

import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.InterceptMark;
import com.qidiangk.smart.maxkb.config.MaxKBTokenInterceptor;

import java.lang.annotation.*;

/**
 * @author mr.g
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@InterceptMark
public @interface Token {

    String[] include() default {"/**"};

    String[] exclude() default {};

    Class<? extends BasePathMatchInterceptor> handler() default MaxKBTokenInterceptor.class;

}
