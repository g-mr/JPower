package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型
 *
 * @author mr.g
 * @date 16:14 2020-05-19
 **/
@AllArgsConstructor
@Getter
public enum ActivationStatusEnum {

    /**
     * 激活
     **/
    ACTIVATION_YES(1, "激活"),
    /**
     * 未激活
     **/
    ACTIVATION_NO(0, "未激活");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        ActivationStatusEnum[] businessModeEnums = values();
        for (ActivationStatusEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
