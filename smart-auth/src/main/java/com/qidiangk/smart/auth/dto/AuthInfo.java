package com.qidiangk.smart.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 鉴权信息
 *
 * @author mr.g
 */
@Data
public class AuthInfo {

    @Schema(description = "令牌")
    private String accessToken;
    @Schema(description = "令牌类型")
    private String tokenType;
    @Schema(description = "刷新令牌")
    private String refreshToken;
    @Schema(description = "过期时间")
    private long expiresIn;

}
