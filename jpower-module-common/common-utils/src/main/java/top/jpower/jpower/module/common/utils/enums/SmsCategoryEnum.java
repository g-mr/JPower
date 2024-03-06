package top.jpower.jpower.module.common.utils.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.jpower.module.common.utils.Fc;

import java.util.Arrays;

/**
 * @author mr.g
 * @date 2024/3/6 10:37 AM
 */
@Getter
@AllArgsConstructor
public enum SmsCategoryEnum {

    ALI("ali", "阿里");

    private final String code;
    private final String val;

    public static SmsCategoryEnum getEnum(String code){
        return Arrays.stream(values()).filter(e-> Fc.equalsValue(e.code, code)).findFirst().orElse(null);
    }

}
