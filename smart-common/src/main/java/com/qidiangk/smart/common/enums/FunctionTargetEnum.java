package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 功能打开方式
 *
 * @author mr.g
 **/
@AllArgsConstructor
@Getter
public enum FunctionTargetEnum {

    /**
     * _self
     **/
    SELF("_self", "_self"),
    /**
     * _top
     **/
    TOP("_top", "_top"),
    /**
     * _blank
     **/
    BLANK("_blank", "_blank"),
    /**
     * _parent
     **/
    PARENT("_parent", "_parent");

    private final String value;
    private final String name;

    public static String getName(String value) {

        FunctionTargetEnum[] businessModeEnums = values();
        for (FunctionTargetEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
