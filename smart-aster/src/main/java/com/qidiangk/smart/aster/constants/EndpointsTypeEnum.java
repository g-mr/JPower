package com.qidiangk.smart.aster.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EndpointsTypeEnum {

    /**
     * 坐席
     */
    ATTEND("attend"),
    /**
     * 线路
     */
    LINE("line");

    private final String name;

}
