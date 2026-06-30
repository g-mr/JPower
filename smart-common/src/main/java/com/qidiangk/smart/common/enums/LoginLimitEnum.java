package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录模式
 *
 * @author mr.g
 **/
@AllArgsConstructor
@Getter
public enum LoginLimitEnum {

    /**
     * 不限制
     **/
    NONE("NONE", "不限制"),
    /**
     * 单模式
     **/
    ONE("ONE", "单模式"),
    /**
     * 挤掉模式
     **/
    SQUEEZE("SQUEEZE", "挤掉模式");

    private final String value;
    private final String name;

    public static String getName(String value) {
        LoginLimitEnum[] businessModeEnums = values();
        for (LoginLimitEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
