package com.qidiangk.smart.aster.constants;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NoticeStatusEnum {

    READY("ready", "开始"),
    RING("ring", "响铃"),
    ANSWER("answer", "接听"),
    HOLD("hold", "通话保持"),
    UN_HOLD("unHold", "通话取消保持"),
    HANG("bye", "挂断"),
    TRANSFER("transfer", "转接"),
    UN_TRANSFER("unTransfer", "结束转接");

    /**
     * 类型
     */
    @EnumValue
    @JsonValue
    private final String value;
    /**
     * 类型名
     */
    private final String name;

}