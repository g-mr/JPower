package com.qidiangk.smart.aster.constants;

import com.mybatisflex.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EndpointsAuthTypeEnum {

    /**
     * MD5
     */
    MD5("md5"),
    /**
     * 用户密码
     */
    USERPASS("userpass"),
    /**
     * 谷歌OAuth
     */
    GOOGLE_OAUTH("google_oauth");

    @EnumValue
    private final String name;

}
