package top.jpower.jpower.auth.granter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.core.exception.handler.BusinessException;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.redis.RedisUtil;
import top.jpower.jpower.utils.TokenUtil;

import static top.jpower.jpower.auth.granter.CaptchaTokenGranter.GRANT_TYPE;

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
