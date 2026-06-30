package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否YN
 *
 * @author mr.g
 */
@AllArgsConstructor
@Getter
public enum YNEnum {

    /**
     * 是
     **/
    Y("Y", "是"),
    /**
     * 否
     **/
    N("N", "否");

    private final String value;
    private final String name;

    public static String getName(String value) {
        YNEnum[] businessModeEnums = values();
        for (YNEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
