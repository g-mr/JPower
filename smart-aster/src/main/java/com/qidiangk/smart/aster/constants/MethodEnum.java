package com.qidiangk.smart.aster.constants;

import cn.hutool.http.Method;
import com.qidiangk.smart.common.enums.ArrayValuable;

import java.util.Arrays;

public enum MethodEnum implements ArrayValuable<String> {

    GET, POST, HEAD, OPTIONS, PUT, DELETE, TRACE, CONNECT, PATCH;

    public Method toHuTool(){
        return Method.valueOf(this.name());
    }

    @Override
    public String[] array() {
        return Arrays.stream(values()).map(MethodEnum::name).toArray(String[]::new);
    }
}
