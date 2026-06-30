package com.qidiangk.smart.common.validated;

import cn.hutool.core.text.CharSequenceUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import top.jpower.core.util.support.JpowerSpelExpressionParser;

public class SpELValidator implements ConstraintValidator<SpEL, String> {

    private final SpelExpressionParser parser = new JpowerSpelExpressionParser();

    @Override
    public void initialize(SpEL annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果为空，默认不校验，即校验通过
        if (CharSequenceUtil.isEmpty(value)) {
            return true;
        }

        // 校验
        try {
            parser.parseExpression(value);
            return true;
        } catch (ParseException e){
            return false;
        }
    }

}
