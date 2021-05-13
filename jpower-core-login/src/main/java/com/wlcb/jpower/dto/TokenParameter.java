package com.wlcb.jpower.dto;

import com.wlcb.jpower.module.common.auth.SecureConstant;
import com.wlcb.jpower.utils.TokenUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 * @date 2021-05-12 23:04
 */
@Data
@ApiModel("登录信息")
public class TokenParameter implements Serializable {
    private static final long serialVersionUID = -4699837505759904032L;

    @ApiModelProperty("租户编码")
    String tenantCode;
    @ApiModelProperty("账号")
    String loginId;
    @ApiModelProperty("密码")
    String passWord;
    @ApiModelProperty("授权类型 (密码登录=password、验证码登录=captcha、第三方平台登录=otherCode、手机号验证码登录=phone、刷新token=refresh_token)")
    String grantType;
    @ApiModelProperty("刷新token   token过期时用刷新token获取新token时必填")
    String refreshToken;
    @ApiModelProperty("手机号   grantType=phone时必填")
    String phone;
    @ApiModelProperty("手机号验证码   grantType=phone时必填")
    String phoneCode;
    @ApiModelProperty("第三方平台标识  grantType=otherCode时必填")
    String otherCode;
    @ApiModelProperty(value = "用户类型   具体值由后端提供",name = TokenUtil.USER_TYPE_HEADER_KEY)
    String userType;
    @ApiModelProperty(value = "客户端识别码",name = SecureConstant.BASIC_HEADER_KEY)
    String authorization;
    @ApiModelProperty(value = "验证码key  grantType=captcha时必填",name = TokenUtil.CAPTCHA_HEADER_KEY)
    String captchaKey;
    @ApiModelProperty(value = "验证码key  grantType=captcha时必填",name = TokenUtil.CAPTCHA_HEADER_CODE)
    String captchaCode;
}
