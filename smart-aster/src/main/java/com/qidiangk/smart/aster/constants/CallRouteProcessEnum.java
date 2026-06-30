package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.qidiangk.smart.common.enums.ArrayValuable;

import java.util.Arrays;

/**
 * 呼叫类型枚举
 */
@AllArgsConstructor
@Getter
public enum CallRouteProcessEnum implements ArrayValuable<Integer> {

    INTEND(1, "呼入"),
    OUTEND(2, "呼出");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(CallRouteProcessEnum::getValue).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static CallRouteProcessEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallRouteProcessEnum.values());
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
