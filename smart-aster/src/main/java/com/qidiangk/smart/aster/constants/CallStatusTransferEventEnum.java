package com.qidiangk.smart.aster.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CallStatusTransferEventEnum {

    /**
     * 转人工开始
     */
    START("START"),
    /**
     * 转人工客服响铃
     */
    RING("RING"),
    /**
     * 转人工客服未接听
     */
    NO_ANSWER("NO_ANSWER"),
    /**
     * 转人工客服接听
     */
    CONNECT("ANSWER"),
    /**
     * 转人工结束
     */
    END("END");

    @JsonValue
    private final String value;
}
