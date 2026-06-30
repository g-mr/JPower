package com.qidiangk.smart.common.validated;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({
        ElementType.METHOD,
        ElementType.FIELD,
        ElementType.ANNOTATION_TYPE,
        ElementType.CONSTRUCTOR,
        ElementType.PARAMETER,
        ElementType.TYPE_USE
})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(
        validatedBy = SpELValidator.class
)
public @interface SpEL {

    String message() default "不是标准的SpEL表达式";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
