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
public enum CallStateEnum {

    START(0, "开始"),
    RING(1, "响铃"),
    ANSWER(2, "接听"),
    HOLD(3, "通话保持"),
    HANG(10, "挂断"),
    EXTEN_RING(101, "分机响铃"),
    EXTEN_ANSWER(102, "分机接听");

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

    public static CallStateEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallStateEnum.values());
    }

}
