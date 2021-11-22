package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.auth.AuthUserInfo;
import com.wlcb.jpower.auth.TokenGranter;
import com.wlcb.jpower.cache.UserCache;
import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.module.base.enums.JpowerError;
import com.wlcb.jpower.module.base.exception.JpowerAssert;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JwtUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.utils.AuthUtil;
import com.wlcb.jpower.utils.TokenUtil;
import com.wlcb.jpower.vo.UserVo;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.wlcb.jpower.auth.granter.RefreshTokenGranter.GRANT_TYPE;

/**
 * @Author 郭丁志
 * @Description //TODO 刷新token默认实现类
 * 		如果需要自定义实现刷新token继承该类实现grantInfo()即可
 * 			例如根据不同的用户类型查询不同的表
 * @Date 00:50 2020-07-28
 **/
@Component(GRANT_TYPE)
public class RefreshTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String grantType = tokenParameter.getGrantType();
		String refreshToken = tokenParameter.getRefreshToken();
		//业务扩展字段
		String userType = tokenParameter.getUserType();
		if (Fc.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = JwtUtil.parseJWT(refreshToken);
			JpowerAssert.notTrue(Fc.isNull(claims), JpowerError.BUSINESS, TokenUtil.TOKEN_EXPIRED);
			String tokenType = Fc.toStr(claims.get(TokenConstant.TOKEN_TYPE));
			if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
				String userId = Fc.toStr(claims.get(TokenConstant.USER_ID));

				if (!Fc.isNull(authUserInfo)){
					return authUserInfo.getRefreshUserInfo(userType,userId);
				}else {
					UserVo result = UserCache.getById(userId);
					return AuthUtil.toUserInfo(result);
				}
			}
		}
		return null;
	}

}
