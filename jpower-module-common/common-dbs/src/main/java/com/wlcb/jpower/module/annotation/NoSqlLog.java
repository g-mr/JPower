package com.wlcb.jpower.module.annotation;

import java.lang.annotation.*;

/**
 * @author mr.gmac
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface NoSqlLog {

}
