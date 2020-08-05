package com.wlcb.jpower.auth.utils;

import com.wlcb.jpower.auth.AuthInfo;
import com.wlcb.jpower.module.common.auth.TokenConstant;
import com.wlcb.jpower.module.common.auth.TokenInfo;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;

import java.util.Map;

/**
 * @ClassName TokenUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-07-27 22:01
 * @Version 1.0
 */
public class TokenUtil {

    public final static String CAPTCHA_HEADER_KEY = "Captcha-Key";
    public final static String CAPTCHA_HEADER_CODE = "Captcha-Code";
    public final static String CAPTCHA_NOT_CORRECT = "验证码不正确";
    public final static String PHONE_NOT_CORRECT = "短信验证码不正确";
    public final static String USER_TYPE_HEADER_KEY = "User-Type";
    public final static String DEFAULT_USER_TYPE = "web";
    public final static String USER_NOT_FOUND = "用户名或密码错误";
    public final static String USER_NOT_ACTIVATION = "用户尚未激活";

    /**
     * 创建认证token
     *
     * @param userInfo 用户信息
     * @return token
     */
    public static AuthInfo createAuthInfo(UserInfo userInfo) {

        //设置jwt参数
        Map<String, Object> param = Fc.toMap(userInfo);
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);

        TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.ACCESS_TOKEN);
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUser(userInfo);
        authInfo.setAccessToken(accessToken.getToken());
        authInfo.setExpiresIn(accessToken.getExpire());
        authInfo.setRefreshToken(createRefreshToken(userInfo).getToken());
        authInfo.setTokenType(TokenConstant.JPOWER);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param userInfo 用户信息
     * @return refreshToken
     */
    private static TokenInfo createRefreshToken(UserInfo userInfo) {
        Map<String, Object> param = ChainMap.newMap();
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN);
        param.put(TokenConstant.USER_ID, userInfo.getUserId());
        return SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.REFRESH_TOKEN);
    }

}
