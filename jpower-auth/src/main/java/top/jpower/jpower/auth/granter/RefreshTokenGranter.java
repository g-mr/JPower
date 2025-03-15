package top.jpower.jpower.auth.granter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.auth.AuthUserInfo;
import top.jpower.jpower.auth.TokenGranter;
import top.jpower.jpower.cache.UserCache;
import top.jpower.jpower.dto.TokenParameter;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.dbs.common.auth.UserInfo;
import top.jpower.core.dbs.common.utils.JwtUtil;
import top.jpower.jpower.utils.TokenUtil;
import top.jpower.jpower.utils.UserUtil;
import top.jpower.jpower.vo.UserVo;

import static top.jpower.jpower.auth.granter.RefreshTokenGranter.GRANT_TYPE;

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
					UserVo result = UserCache.getById(userId);
					return UserUtil.toUserInfo(result);
				}
			}
		}
		return null;
	}

}
