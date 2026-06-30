package com.qidiangk.smart.common.validated;

import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import top.jpower.core.util.utils.Fc;

import java.util.List;

/**
 * 字符串分割校验
 *
 * @author mr.g
 */
public class NotBlankSpiltValidator implements ConstraintValidator<NotBlankSpilt, String> {

    private NotBlankSpilt annotation;

    @Override
    public void initialize(NotBlankSpilt annotation) {
        this.annotation = annotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StrUtil.isBlank(value)) {
            return false;
        }

        List<String> values = StrUtil.split(value, annotation.split());

        return Fc.isNotEmpty(values);
    }

}
