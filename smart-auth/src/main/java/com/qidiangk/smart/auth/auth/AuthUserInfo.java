package com.qidiangk.smart.auth.auth;

import org.springframework.context.annotation.Configuration;
import com.qidiangk.smart.auth.dto.TokenParameter;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

/**
 * 授权需求实现<br/>
 * 如果有业务上的特殊需求，继承该类实现即可；继承的必须加上@Configuration注解
 *
 * @author mr.g
 **/
@Configuration
public interface AuthUserInfo {

    /**
     * 密码登陆各自业务实现
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
	CoreUserDTO getPasswordUserInfo(TokenParameter tokenParameter);

    /**
     * 验证码登陆各自业务实现
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
	CoreUserDTO getCaptchaUserInfo(TokenParameter tokenParameter);

    /**
     * 第三方Code各自业务实现
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
	CoreUserDTO getOtherCodeUserInfo(TokenParameter tokenParameter);

    /**
     * 刷新token各自业务实现
     *
     * @author mr.g
     * @param userType 请求头（HEADER）User-Type参数值
     * @param userId 用户ID
     * @return 返回用户信息
     **/
	CoreUserDTO getRefreshUserInfo(String userType,Long userId);

    /**
     * 手机号登录
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
	CoreUserDTO getPhoneUserInfo(TokenParameter tokenParameter);

}
