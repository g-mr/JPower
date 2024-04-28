package top.jpower.jpower.annotation;

import java.lang.annotation.*;

/**
 * @author mr.g
 *
 * 主键
 */
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JId {
}
