package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.AuthUserInfo;
import com.wlcb.jpower.auth.TokenGranter;
import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.utils.AuthUtil;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dbs.entity.TbCoreUser;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wlcb.jpower.auth.granter.PasswordTokenGranter.GRANT_TYPE;


/**
 * @Author 郭丁志
 * @Description //TODO 密码登录默认实现类
 * @Date 00:50 2020-07-28
 **/
@Component(GRANT_TYPE)
public class PasswordTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "password";

	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String account = tokenParameter.getLoginId();
		String password = tokenParameter.getPassWord();
		String tenantCode = tokenParameter.getTenantCode();
		if (Fc.isNoneBlank(account, password)) {
			if (!Fc.isNull(authUserInfo)){
				return authUserInfo.getPasswordUserInfo(tokenParameter);
			}else {
				TbCoreUser result = UserCache.queryUserByLoginIdPwd(account,password,tenantCode);
				return AuthUtil.toUserInfo(result);
			}
		}
		return null;
	}

}

