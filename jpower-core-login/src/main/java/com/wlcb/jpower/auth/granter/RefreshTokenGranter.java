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

import com.wlcb.jpower.auth.utils.TokenUtil;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.auth.TokenConstant;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.dbs.dao.core.user.TbCoreUserDao;
import com.wlcb.jpower.module.dbs.entity.core.user.TbCoreUser;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author 郭丁志
 * @Description //TODO 刷新token默认实现类
 * 		如果需要自定义实现刷新token继承该类实现grantInfo()即可
 * 			例如根据不同的用户类型查询不同的表
 * @Date 00:50 2020-07-28
 **/
@Component
public class RefreshTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	@Autowired
	private TbCoreUserDao coreUserDao;
	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public UserInfo grant(ChainMap tokenParameter) {
		String grantType = tokenParameter.getStr("grantType");
		String refreshToken = tokenParameter.getStr("refreshToken");
		//业务扩展字段
		String userType = tokenParameter.getStr("userType");
		if (Fc.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = SecureUtil.parseJWT(refreshToken);
			JpowerAssert.notTrue(Fc.isNull(claims), JpowerError.BUSINESS, TokenUtil.TOKEN_EXPIRED);
			String tokenType = Fc.toStr(claims.get(TokenConstant.TOKEN_TYPE));
			if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
				String userId = Fc.toStr(claims.get(TokenConstant.USER_ID));

				if (!Fc.isNull(authUserInfo)){
					return authUserInfo.getRefreshUserInfo(userType,userId);
				}else {
					TbCoreUser result = coreUserDao.getById(userId);
					return TokenGranterBuilder.toUserInfo(result);
				}
			}
		}
		return null;
	}
}
