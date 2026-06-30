package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.core.util.utils.Fc;

import java.util.Arrays;

/**
 * @author mr.g
 * @date 2024/3/6 10:37 AM
 */
@Getter
@AllArgsConstructor
public enum SmsCategoryEnum {

    /**
     * 阿里
     **/
    ALI("ali", "阿里");

    private final String code;
    private final String val;

    public static SmsCategoryEnum getEnum(String code){
        return Arrays.stream(values()).filter(e-> Fc.equalsValue(e.code, code)).findFirst().orElseThrow(() -> new RuntimeException("短信类型不合法"));
    }

}
