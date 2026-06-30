package com.qidiangk.smart.aster.constants;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * @author mr.g
 */

@Getter
public enum VariableNameEnum {

    /**
     * 文件路径
     */
    FILE_PATH("filepath"),
    /**
     * 转人工文件录音
     */
    TRANSFER_FILE("transferFile"),
    /**
     * 路由ID
     */
    ROUTE_ID("routeId"),
    /**
     * 线路ID
     */
    LINE_ID("lineId"),
    /**
     * 外呼参数
     */
    PARAMS("params");

    private final String name;

    VariableNameEnum(String name) {
        this.name = name;
    }

    public static List<String> toArray() {
        return Arrays.stream(VariableNameEnum.values()).map(VariableNameEnum::toValue).toList();
    }

    public String toValue() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
