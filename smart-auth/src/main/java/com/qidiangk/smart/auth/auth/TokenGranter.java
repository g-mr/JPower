package com.qidiangk.smart.auth.auth;

import com.qidiangk.smart.auth.dto.TokenParameter;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

/**
 * 授权认证统一接口
 *
 * @author mr.g
 **/
public interface TokenGranter {

	/**
	 * 获取用户信息
	 *
	 * @param tokenParameter 授权参数
	 * @return UserInfo
	 */
	CoreUserDTO grant(TokenParameter tokenParameter) ;

}
