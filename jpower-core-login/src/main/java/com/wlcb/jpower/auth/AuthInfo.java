package com.wlcb.jpower.auth;

import com.wlcb.jpower.module.common.auth.UserInfo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName AuthInfo
 * @Description TODO 鉴权信息
 * @Author 郭丁志
 * @Date 2020-07-27 23:51
 * @Version 1.0
 */
@Data
public class AuthInfo {

    /** 用户信息 **/
    @ApiModelProperty("用户信息")
    private UserInfo user;
    @ApiModelProperty("令牌")
    private String accessToken;
    /** 令牌类型 **/
    private String tokenType;
    /** 刷新令牌 **/
    private String refreshToken;
    /** 过期时间 **/
    private long expiresIn;

}
