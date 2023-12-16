package top.jpower.jpower.module.base.annotation;

import java.lang.annotation.*;

/**
 * @author mr.g
 * @date 2022/9/25 18:30
 */
@Documented
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.SOURCE)
public @interface LoaderService {

    /**
     * 注明这个类继承的哪个实现。
     *
     * @author mr.g
     **/
    Class<?>[] value();

}
