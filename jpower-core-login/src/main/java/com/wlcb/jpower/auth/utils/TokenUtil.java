package com.wlcb.jpower.auth.utils;

import com.wlcb.jpower.auth.AuthInfo;
import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.module.common.auth.ClientDetails;
import com.wlcb.jpower.module.common.auth.TokenInfo;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.BeanUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;

import java.util.Map;

/**
 * @ClassName TokenUtil
 * @Description TODO 生成token
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
    public final static String TOKEN_EXPIRED = "token已过期，请重新登陆";
    public final static String USER_NOT_ACTIVATION = "用户尚未激活";


    /**
     * @author 郭丁志
     * @Description //TODO 获取客户端信息
     * @date 23:08 2020/10/17 0017
     * @return com.wlcb.jpower.module.common.auth.ClientDetails
     */
    private static ClientDetails getClientDetails(){
        String[] tokens = SecureUtil.extractAndDecodeHeader();
        assert tokens.length == 2;
        String clientCode = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
        TbCoreClient client = SystemCache.getClientByClientCode(clientCode);
        ClientDetails clientDetails = Fc.notNull(client)?BeanUtil.copy(client,ClientDetails.class):null;

        // 校验客户端信息
        if (!SecureUtil.validateClient(clientDetails, clientCode, clientSecret)) {
            throw new RuntimeException("客户端认证失败!");
        }

        return clientDetails;
    }

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

        ClientDetails clientDetails = getClientDetails();
        userInfo.setClientCode(clientDetails.getClientCode());

        TokenInfo accessToken = SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.ACCESS_TOKEN,clientDetails);
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUser(userInfo);
        authInfo.setAccessToken(accessToken.getToken());
        authInfo.setExpiresIn(accessToken.getExpire());
        authInfo.setRefreshToken(createRefreshToken(userInfo,clientDetails).getToken());
        authInfo.setTokenType(TokenConstant.JPOWER);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param userInfo 用户信息
     * @return refreshToken
     */
    private static TokenInfo createRefreshToken(UserInfo userInfo,ClientDetails clientDetails) {
        Map<String, Object> param = ChainMap.newMap();
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN);
        param.put(TokenConstant.USER_ID, userInfo.getUserId());
        return SecureUtil.createJWT(param, "audience", "issuser", TokenConstant.REFRESH_TOKEN,clientDetails);
    }

}
