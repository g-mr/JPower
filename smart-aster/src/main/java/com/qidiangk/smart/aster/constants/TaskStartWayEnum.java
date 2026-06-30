package com.qidiangk.smart.aster.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 外呼任务启动方式
 *
 * @author mr.g
 */
@Getter
@AllArgsConstructor
public enum TaskStartWayEnum {

    /**
     * 手动启动
     */
    MANUALLY(1, "手动启动"),
    /**
     * 定时启动
     */
    TIMING(2, "定时启动");

    /**
     * 状态值
     */
    @EnumValue
    @JsonValue
    private final Integer value;
    /**
     * 状态名
     */
    private final String name;

}