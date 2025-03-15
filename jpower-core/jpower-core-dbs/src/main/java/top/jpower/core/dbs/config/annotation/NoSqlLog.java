package top.jpower.core.dbs.config.annotation;

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
