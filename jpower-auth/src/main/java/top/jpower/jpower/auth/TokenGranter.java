package top.jpower.jpower.auth;

import top.jpower.jpower.dto.TokenParameter;
import top.jpower.user.api.dto.CoreUserDTO;

/**
 * @Author 郭丁志
 * @Description //TODO 授权认证统一接口
 * @Date 00:34 2020-07-28
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
