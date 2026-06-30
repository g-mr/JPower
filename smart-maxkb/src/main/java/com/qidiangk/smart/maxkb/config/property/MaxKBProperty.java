package com.qidiangk.smart.maxkb.config.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "maxkb")
public class MaxKBProperty {

    /**
     * 账号
     */
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 地址
     */
    private String baseUrl;
    /**
     * 外网访问地址
     */
    private String externalBaseUrl;

}
