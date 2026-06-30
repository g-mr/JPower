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
public enum OutCallStatusEnum implements ArrayValuable<Integer> {

    PLAYED(1, "已拨打"),
    UN_PLAYING(0, "未拨打"),
    PLAYING(2, "拨打中");

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(OutCallStatusEnum::getValue).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer value;
    /**
     * 类型名
     */
    private final String name;

    public static OutCallStatusEnum valueOf(Integer value) {
        return ArrayUtil.firstMatch(userType -> userType.getValue().equals(value), OutCallStatusEnum.values());
    }

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

}
