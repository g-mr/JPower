package com.wlcb.jpower.auth.granter;

import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;

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
	UserInfo grant(ChainMap tokenParameter) ;

}
