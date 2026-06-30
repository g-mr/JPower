package com.qidiangk.smart.aster.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum QueueStrategyEnum {

    /**
     * 同时振铃所有空闲坐席
     */
    RINGALL("ringall"),
    /**
     * 振铃最近一次接起呼叫时间最早的坐席
     */
    LEASTRECENT("leastrecent"),
    /**
     * 振铃接起呼叫总数最少的坐席
     */
    FEWESTCALLS("fewestcalls"),
    /**
     * 随机振铃一个空闲坐席
     */
    RANDOM("random"),
    /**
     *  轮询，并记住上次分配到的位置
     */
    RRMEMORY("rrmemory"),
    /**
     * 线性排序（按顺序尝试每个成员）
     */
    LINEAR("linear"),
    /**
     * 加权随机（根据权重随机选择）
     */
    WRANDOM("wrandom"),
    /**
     * 按坐席顺序轮询
     */
    RRORDERED("rrordered");


    @EnumValue
    @JsonValue
    private final String name;

}
