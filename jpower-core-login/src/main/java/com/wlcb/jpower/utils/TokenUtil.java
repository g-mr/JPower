package com.wlcb.jpower.utils;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dto.AuthInfo;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.TokenInfo;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.DateUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SecureUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
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
    public static TbCoreClient getClientDetails(){
        String[] tokens = SecureUtil.getClientInfo();
        assert tokens.length == 2;
        String clientCode = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
        TbCoreClient client = SystemCache.getClientByClientCode(clientCode);

        // 校验客户端信息
        if (!validateClient(client, clientCode, clientSecret)) {
            throw new BusinessException("客户端认证失败!");
        }

        return client;
    }

    /**
     * 校验Client
     *
     * @param clientCode   客户端code
     * @param clientSecret 客户端密钥
     * @return boolean
     */
    public static boolean validateClient(TbCoreClient client, String clientCode, String clientSecret) {
        if (Fc.notNull(client)) {
            return StringUtil.equals(clientCode, client.getClientCode()) && StringUtil.equals(clientSecret, client.getClientSecret());
        }
        return false;
    }

    /**
     * 获取过期时间
     * @Author mr.g
     * @param tokenValidity
     * @return long
     **/
    private static long getExpire(Long tokenValidity) {
        // 默认时间为明天
        return Fc.isNull(tokenValidity) || tokenValidity == 0 ?
                DateUtil.tomorrow().getTime() - System.currentTimeMillis() :
                tokenValidity;
    }

    /**
     * 创建认证token
     *
     * @param userInfo 用户信息
     * @return token
     */
    public static AuthInfo createAuthInfo(UserInfo userInfo) {

        TbCoreClient client = getClientDetails();
        assert client != null;
        userInfo.setClientCode(client.getClientCode());

        //设置jwt参数
        Map<String, Object> param = Fc.toMap(userInfo);
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        param.put(TokenConstant.CLIENT_CODE, client.getClientCode());

        TokenInfo accessToken = SecureUtil.createJWT(param, getExpire(client.getAccessTokenValidity()));
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUser(userInfo);
        authInfo.setAccessToken(accessToken.getToken());
        authInfo.setExpiresIn(accessToken.getExpire());
        authInfo.setRefreshToken(createRefreshToken(userInfo,client).getToken());
        authInfo.setTokenType(TokenConstant.JPOWER);
        AuthUtil.cacheAuth(authInfo);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param userInfo 用户信息
     * @return refreshToken
     */
    private static TokenInfo createRefreshToken(UserInfo userInfo,TbCoreClient client) {
        return SecureUtil.createJWT(ChainMap.init()
                .set(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN)
                .set(TokenConstant.USER_ID, userInfo.getUserId())
                .set(TokenConstant.CLIENT_CODE, client.getClientCode())
                ,getExpire(client.getRefreshTokenValidity()));
    }

}
