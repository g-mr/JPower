package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import com.wlcb.jpower.module.mp.support.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author 郭丁志
 * @Description //TODO 密码登录默认实现类
 * @Date 00:50 2020-07-28
 **/
@Component("passwordTokenGranter")
public class PasswordTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "password";

	@Autowired
	private TbCoreUserDao coreUserDao;
	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public UserInfo grant(ChainMap tokenParameter) {
		String account = tokenParameter.getStr("account");
		String password = tokenParameter.getStr("password");
		if (Fc.isNoneBlank(account, password)) {
			if (!Fc.isNull(authUserInfo)){
				return authUserInfo.getPasswordUserInfo(tokenParameter);
			}else {
				TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
						.lambda().eq(TbCoreUser::getLoginId,account).eq(TbCoreUser::getPassword, DigestUtil.encrypt(password)));
				return TokenGranterBuilder.toUserInfo(result);
			}
		}
		return null;
	}

}
