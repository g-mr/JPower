package top.jpower.auth.auth.granter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.core.util.utils.Fc;
import top.jpower.auth.auth.AuthUserInfo;
import top.jpower.auth.auth.TokenGranter;
import top.jpower.auth.dto.TokenParameter;
import top.jpower.user.api.cache.UserCache;
import top.jpower.user.api.dto.CoreUserDTO;
import top.jpower.user.api.dto.ValidatePasswordDTO;
import top.jpower.user.api.feign.UserClient;

import static top.jpower.auth.auth.granter.PasswordTokenGranter.GRANT_TYPE;


/**
 * 密码登录默认实现类
 *
 * @author mr.g
 **/
@Component(GRANT_TYPE)
@RequiredArgsConstructor
public class PasswordTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "password";

	@Autowired(required = false)
	private AuthUserInfo authUserInfo;
	private final UserClient userClient;

	@Override
	public CoreUserDTO grant(TokenParameter tokenParameter) {
		String account = tokenParameter.getLoginId();
		String password = tokenParameter.getPassWord();
		String tenantCode = tokenParameter.getTenantCode();
		if (Fc.isNoneBlank(account, password)) {
			if (!Fc.isNull(authUserInfo)){
				return authUserInfo.getPasswordUserInfo(tokenParameter);
			}else {
				if (userClient.validatePassword(new ValidatePasswordDTO().setPassword(password).setAccount(account).setTenantCode(tenantCode))
						.getData()){
					return UserCache.getUserByLoginId(account, tenantCode);
				}
			}
		}
		return null;
	}

}

