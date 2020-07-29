/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.DigestUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import com.wlcb.jpower.module.mp.support.Condition;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;


/**
 * @Author 郭丁志
 * @Description //TODO 密码登录默认实现类
 * @Date 00:50 2020-07-28
 **/
@Component("passwordTokenGranter")
@AllArgsConstructor
public class PasswordTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "password";

	private TbCoreUserDao coreUserDao;

	@Override
	public UserInfo grant(ChainMap tokenParameter) {
		String account = tokenParameter.getStr("account");
		String password = tokenParameter.getStr("password");
		UserInfo userInfo = null;
		if (Fc.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = tokenParameter.getStr("userType");
			//不同的用户类型都可以在这里写逻辑，或者重写这个类的方法
			if (Fc.equals(userType,"web")){
				TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
						.lambda().eq(TbCoreUser::getLoginId,account).eq(TbCoreUser::getPassword, DigestUtil.encrypt(password)));
				return TokenGranterBuilder.toUserInfo(result);
			}else {
				TbCoreUser result = coreUserDao.getOne(Condition.<TbCoreUser>getQueryWrapper()
						.lambda().eq(TbCoreUser::getLoginId,account).eq(TbCoreUser::getPassword, DigestUtil.encrypt(password)));
				return TokenGranterBuilder.toUserInfo(result);
			}
		}
		return userInfo;
	}

}
