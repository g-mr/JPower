package com.wlcb.jpower.auth;

import com.wlcb.jpower.dto.TokenParameter;
import com.wlcb.jpower.module.common.auth.UserInfo;
import org.springframework.context.annotation.Configuration;

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
    UserInfo getPasswordUserInfo(TokenParameter tokenParameter);

    /**
     * 验证码登陆各自业务实现
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
    UserInfo getCaptchaUserInfo(TokenParameter tokenParameter);

    /**
     * 第三方Code各自业务实现
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
    UserInfo getOtherCodeUserInfo(TokenParameter tokenParameter);

    /**
     * 刷新token各自业务实现
     *
     * @author mr.g
     * @param userType 请求头（HEADER）User-Type参数值
     * @param userId 用户ID
     * @return 返回用户信息
     **/
    UserInfo getRefreshUserInfo(String userType,String userId);

    /**
     * 手机号登录
     *
     * @author mr.g
     * @param tokenParameter 前端请求参数
     * @return 返回用户信息
     **/
    UserInfo getPhoneUserInfo(TokenParameter tokenParameter);

}
