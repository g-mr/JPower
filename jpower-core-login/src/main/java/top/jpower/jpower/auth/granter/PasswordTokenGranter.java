package top.jpower.jpower.auth.granter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dbs.entity.TbCoreUser;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.jpower.feign.UserClient;
import top.jpower.jpower.module.common.auth.UserInfo;
import top.jpower.jpower.module.common.utils.Fc;
import top.jpower.jpower.utils.UserUtil;

import static top.jpower.jpower.auth.granter.PasswordTokenGranter.GRANT_TYPE;


/**
 * @Author 郭丁志
 * @Description //TODO 密码登录默认实现类
 * @Date 00:50 2020-07-28
 **/
@Component(GRANT_TYPE)
@RequiredArgsConstructor
public class PasswordTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "password";

	@Autowired(required = false)
	private AuthUserInfo authUserInfo;
	private final UserClient userClient;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String account = tokenParameter.getLoginId();
		String password = tokenParameter.getPassWord();
		String tenantCode = tokenParameter.getTenantCode();
		if (Fc.isNoneBlank(account, password)) {
			if (!Fc.isNull(authUserInfo)){
				return authUserInfo.getPasswordUserInfo(tokenParameter);
			}else {
				if (userClient.validatePassword(account,password,tenantCode)){
					TbCoreUser result = UserCache.getUserByLoginId(account, tenantCode);
					return UserUtil.toUserInfo(result);
				}
			}
		}
		return null;
	}

}

