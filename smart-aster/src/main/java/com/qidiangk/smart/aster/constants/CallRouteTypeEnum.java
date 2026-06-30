package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.ArrayUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import com.qidiangk.smart.common.enums.ArrayValuable;

import java.util.Arrays;

/**
 * 全局用户类型枚举
 */
@AllArgsConstructor
@Getter
public enum CallRouteTypeEnum implements ArrayValuable<Integer> {

    MAIN(1, "主流程"),
    CHILDREN(2, "子流程");

    private static final Integer[] ARRAYS = Arrays.stream(values()).map(CallRouteTypeEnum::getValue).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static CallRouteTypeEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), CallRouteTypeEnum.values());
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
