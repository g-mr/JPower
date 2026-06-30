package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.ArrayUtil;
import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 全局呼叫类型枚举
 */
@AllArgsConstructor
@Getter
public enum CallOutTypeEnum {

    OUT_ROBOT(10, "机器人呼出"),
    OUT(21, "分机直拨呼出"),
    OUT_WEB(22, "网页拨号呼出");

    /**
     * 类型
     */
    @EnumValue
    @JsonValue
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static CallOutTypeEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallOutTypeEnum.values());
    }

}
