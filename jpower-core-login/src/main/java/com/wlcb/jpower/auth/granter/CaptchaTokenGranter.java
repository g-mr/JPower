package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.AuthUserInfo;
import com.wlcb.jpower.auth.TokenGranter;
import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.utils.TokenUtil;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wlcb.jpower.auth.granter.CaptchaTokenGranter.GRANT_TYPE;

/**
 * @Author 郭丁志
 * @Description //TODO 验证码登录默认实现类
 * @Date 00:50 2020-07-28
 **/
@Component(GRANT_TYPE)
public class CaptchaTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "captcha";

	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	private PasswordTokenGranter passwordTokenGranter;
	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {

		String key = tokenParameter.getCaptchaKey();
		String code = tokenParameter.getCaptchaCode();
		// 获取验证码
		String redisCode = String.valueOf(redisUtil.get(CacheNames.CAPTCHA_KEY + key));
		// 判断验证码
		if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
			throw new BusinessException(TokenUtil.CAPTCHA_NOT_CORRECT);
		}

		if (!Fc.isNull(authUserInfo)){
			if (Fc.isNoneBlank(tokenParameter.getLoginId(), tokenParameter.getPassWord())) {
				return authUserInfo.getCaptchaUserInfo(tokenParameter);
			}
		}else {
			return passwordTokenGranter.grant(tokenParameter);
		}

		return null;
	}
}
