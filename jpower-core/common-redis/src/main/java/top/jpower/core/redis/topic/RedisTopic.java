package top.jpower.core.redis.topic;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface RedisTopic {

    String[] value();

    boolean isJsonSource() default false;

}
