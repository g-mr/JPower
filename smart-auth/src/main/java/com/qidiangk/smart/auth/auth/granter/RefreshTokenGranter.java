package com.qidiangk.smart.auth.auth.granter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import com.qidiangk.smart.auth.auth.AuthUserInfo;
import com.qidiangk.smart.auth.auth.TokenGranter;
import com.qidiangk.smart.auth.dto.TokenParameter;
import com.qidiangk.smart.auth.utils.TokenUtil;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.Fc;
import com.qidiangk.smart.user.api.cache.UserCache;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

import static com.qidiangk.smart.auth.auth.granter.RefreshTokenGranter.GRANT_TYPE;

/**
 * 刷新token默认实现类
 *
 * @author mr.g
 **/
@Component(GRANT_TYPE)
public class RefreshTokenGranter implements TokenGranter {

	public static final String GRANT_TYPE = "refresh_token";

	@Autowired(required = false)
	private AuthUserInfo authUserInfo;

	@Override
	public CoreUserDTO grant(TokenParameter tokenParameter) {
		String grantType = tokenParameter.getGrantType();
		String refreshToken = tokenParameter.getRefreshToken();
		//业务扩展字段
		String userType = tokenParameter.getUserType();
		if (Fc.isNoneBlank(grantType, refreshToken) && grantType.equals(TokenConstant.REFRESH_TOKEN)) {
			Claims claims = JwtUtil.parseJwt(refreshToken);
			if (Fc.isNull(claims)){
				throw new JpowerException(HttpStatus.PROXY_AUTHENTICATION_REQUIRED.value(), TokenUtil.TOKEN_EXPIRED);
			}
			String tokenType = Fc.toStr(claims.get(TokenConstant.TOKEN_TYPE));
			if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
				Long userId = Fc.toLong(claims.get(TokenConstant.USER_ID));

				if (!Fc.isNull(authUserInfo)){
					return authUserInfo.getRefreshUserInfo(userType,userId);
				}else {
					return UserCache.getById(userId);
				}
			}
		}
		return null;
	}

}
