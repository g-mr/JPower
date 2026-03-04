package top.jpower.jpower.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.jpower.core.auth.utils.constant.SecureConstant;
import top.jpower.jpower.utils.TokenUtil;

import java.io.Serial;
import java.io.Serializable;

/**
 * 登录参数
 *
 * @author mr.g
 */
@Data
@Schema(description = "登录信息")
public class TokenParameter implements Serializable {

    @Serial
	private static final long serialVersionUID = -4699837505759904032L;

    @Schema(description = "租户编码")
    String tenantCode;
    @Schema(description = "账号")
    String loginId;
    @Schema(description = "密码")
    String passWord;
    @Schema(description = "授权类型 (密码登录=password、验证码登录=captcha、第三方平台登录=otherCode、手机号验证码登录=phone、刷新token=refresh_token)")
    String grantType;
    @Schema(description = "刷新token   token过期时用刷新token获取新token时必填")
    String refreshToken;
    @Schema(description = "手机号   grantType=phone时必填")
    String phone;
    @Schema(description = "手机号验证码   grantType=phone时必填")
    String phoneCode;
    @Schema(description = "第三方平台标识  grantType=otherCode时必填")
    String otherCode;
    @Schema(description = "用户类型   具体值由后端提供",name = TokenUtil.USER_TYPE_HEADER_KEY)
    String userType;
    @Schema(description = "客户端识别码",name = SecureConstant.BASIC_HEADER_KEY)
    String authorization;
    @Schema(description = "验证码key  grantType=captcha时必填",name = TokenUtil.CAPTCHA_HEADER_KEY)
    String captchaKey;
    @Schema(description = "验证码key  grantType=captcha时必填",name = TokenUtil.CAPTCHA_HEADER_CODE)
    String captchaCode;
}
