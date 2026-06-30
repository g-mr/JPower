package com.qidiangk.smart.aster.constants;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EndpointsDtmfModeEnum {

    /**
     * RTC
     */
    RFC4733("rfc4733"),
    /**
     * inband
     */
    INBAND("inband"),
    INFO("info"),
    AUTO("auto"),
    AUTO_INFO("auto_info");

    @EnumValue
    private final String name;

}
