package com.qidiangk.smart.common.validated;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import static top.jpower.core.util.constants.StringPool.COMMA;

/**
 * 校验是否是手机号
 *
 * @author mr.g
 */
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
        validatedBy = NotBlankSpiltValidator.class
)
public @interface NotBlankSpilt {

    String message() default "参数不能为空";

    String split() default COMMA;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
