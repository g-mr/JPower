package com.qidiangk.smart.auth.auth.granter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.qidiangk.smart.common.constants.CacheNames;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import com.qidiangk.smart.auth.auth.AuthUserInfo;
import com.qidiangk.smart.auth.auth.TokenGranter;
import com.qidiangk.smart.auth.dto.TokenParameter;
import com.qidiangk.smart.auth.utils.TokenUtil;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

import static com.qidiangk.smart.auth.auth.granter.CaptchaTokenGranter.GRANT_TYPE;

/**
 * 验证码登录默认实现类
 *
 * @author 郭丁志
 **/
@Component(GRANT_TYPE)
public class CaptchaTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "captcha";

	@Autowired
	private RedisService redisService;
	@Autowired
	private PasswordTokenGranter passwordTokenGranter;
	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public CoreUserDTO grant(TokenParameter tokenParameter) {

		String key = tokenParameter.getCaptchaKey();
		String code = tokenParameter.getCaptchaCode();
		// 获取验证码
		String redisCode = redisService.valueOps(String.class).get(CacheNames.CAPTCHA_KEY + key);
		// 判断验证码
		if (code == null || !StringUtil.equalsIgnoreCase(redisCode, code)) {
			throw new BusinessException(TokenUtil.CAPTCHA_NOT_CORRECT);
		}

		if (Fc.notNull(authUserInfo)){
			if (Fc.isNoneBlank(tokenParameter.getLoginId(), tokenParameter.getPassWord())) {
				return authUserInfo.getCaptchaUserInfo(tokenParameter);
			}
		}else {
			return passwordTokenGranter.grant(tokenParameter);
		}

		return null;
	}
}
