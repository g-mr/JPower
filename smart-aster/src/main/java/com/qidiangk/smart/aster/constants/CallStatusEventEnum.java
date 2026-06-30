package com.qidiangk.smart.aster.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallStatusEventEnum {

    /**
     * 	表示一个通话开始
     */
    CALL("CALL"),
    /**
     * 	表示响铃
     */
    RING("RING"),
    /**
     * 	表示通话接听
     */
    ANSWER("ANSWER"),
    /**
     * 	表示一个通话保持
     */
    HOLD("HOLD"),
    /**
     * 	表示一个通话被被机器人转到人工客服
     */
    TRANSFER("TRANSFER"),
    /**
     * 	表示一个通话挂断
     */
    HUANG("HUANG");

    @JsonValue
    private final String value;
}
