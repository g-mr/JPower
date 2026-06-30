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
public enum CallTypeEnum {

    IN(1, "呼入"),
    OUT(2, "呼出"),
    INNER(3, "内部通话");

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

    public static CallTypeEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallTypeEnum.values());
    }

}
