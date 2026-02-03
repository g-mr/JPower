package top.jpower.common.validated;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 验证是否身份证
 *
 * @author mr.g
 */
public class IdCardValidator implements ConstraintValidator<IdCard, String> {

    @Override
    public void initialize(IdCard annotation) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // 如果身份证为空，默认不校验，即校验通过
        if (StrUtil.isBlank(value)) {
            return true;
        }
        // 校验身份证
        return Validator.isCitizenId(value);
    }

}