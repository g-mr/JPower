package com.qidiangk.smart.aster.constants;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 加密模式
 */
@AllArgsConstructor
@Getter
public enum EndpointsMediaEncryptionEnum {

    /**
     * 不开启媒体加密
     */
    NO("no"),
    /**
     * 启用sdes加密
     */
    SDES("sdes"),
    /**
     * 启用dtls加密
     */
    DTLS("dtls");

    @EnumValue
    private final String name;

}
