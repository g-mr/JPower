package com.qidiangk.smart.aster.constants;

import com.qidiangk.smart.common.enums.ArrayValuable;

import java.util.Arrays;

public enum ScriptEnum implements ArrayValuable<String> {

    JS("JS");

    private final String value;

    ScriptEnum(String value) {
        this.value = value;
    }

    // 使用标准方法名
    public String toValue() {
        return value;
    }

    @Override
    public String[] array() {
        return Arrays.stream(values()).map(ScriptEnum::toValue).toArray(String[]::new);
    }
}
