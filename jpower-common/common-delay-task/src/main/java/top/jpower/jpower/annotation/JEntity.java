package top.jpower.jpower.annotation;

import java.lang.annotation.*;

/**
 * @author mr.g
 *
 * 实体注解
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface JEntity {

    String value() default "";

}
