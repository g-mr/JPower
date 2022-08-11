package com.wlcb.jpower.utils;

import com.wlcb.jpower.cache.SystemCache;
import com.wlcb.jpower.dbs.entity.client.TbCoreClient;
import com.wlcb.jpower.dbs.entity.function.TbCoreDataScope;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import com.wlcb.jpower.dto.AuthInfo;
import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.cache.CacheNames;
import com.wlcb.jpower.module.common.redis.RedisUtil;
import com.wlcb.jpower.module.common.support.ChainMap;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.ConstantsEnum;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import com.wlcb.jpower.module.datascope.DataScope;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 生成token工具
 *
 * @author mr.g
 **/
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
     * 获取客户端信息
     * @return 客户端信息
     */
    public static TbCoreClient getClientDetails(){
        String[] tokens = ShieldUtil.getClientInfo();
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
     *
     * @author mr.g
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

        //token过期时间
        long expire = getExpire(client.getAccessTokenValidity());

        AuthInfo authInfo = new AuthInfo();
        authInfo.setUser(userInfo);
        authInfo.setAccessToken(JwtUtil.createJwt(param, expire));
        authInfo.setExpiresIn(expire);
        authInfo.setRefreshToken(createRefreshToken(userInfo,client));
        authInfo.setTokenType(TokenConstant.TOKEN_PREFIX);
        cacheAuth(authInfo,client);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param userInfo 用户信息
     * @return refreshToken
     */
    private static String createRefreshToken(UserInfo userInfo,TbCoreClient client) {
        return JwtUtil.createJwt(ChainMap.<String, Object>create()
                .put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN)
                .put(TokenConstant.USER_ID, userInfo.getUserId())
                .put(TokenConstant.CLIENT_CODE, client.getClientCode()).build()
                ,getExpire(client.getRefreshTokenValidity()));
    }

    /**
     * 缓存鉴权信息
     *
     * @author mr.g
     * @param authInfo 鉴权信息
     **/
    private static void cacheAuth(AuthInfo authInfo,TbCoreClient client) {
        List<TbCoreDataScope> dataScopeRoleList = SystemCache.getDataScopeByRole(authInfo.getUser().getRoleIds(),client.getClientCode());
        List<TbCoreFunction> menuList = SystemCache.getMenuListByRole(authInfo.getUser().getRoleIds(),client.getClientCode());

        Map<String, List<DataScope>> map = ChainMap.<String,List<DataScope>>create().build();
        if (Fc.isNotEmpty(dataScopeRoleList)){
            dataScopeRoleList.forEach(dataScope -> {
                String code = Fc.isNotEmpty(menuList) ? menuList.stream().filter(menu -> Fc.equalsValue(menu.getId(),dataScope.getMenuId())).map(TbCoreFunction::getCode).findFirst().orElse(null) : null;
                if (Fc.isNotBlank(code)){

                    boolean is = true;
                    //角色配置的数据权限比所有角色可执行的权限优先级要高，所以判断有自己的权限的时候就不要全角色执行的权限了
                    if (Fc.equalsValue(dataScope.getAllRole(), ConstantsEnum.YN01.Y.getValue())){
                        is = dataScopeRoleList.stream().noneMatch(scope-> Fc.equalsValue(scope.getAllRole(), ConstantsEnum.YN01.N.getValue()) && Fc.equalsValue(dataScope.getScopeClass(), scope.getScopeClass()));
                    }

                    if (is){
                        List<DataScope> dataScopeList = map.get(code);
                        if (Fc.isEmpty(dataScopeList)){
                            dataScopeList = new ArrayList<>();
                        }
                        dataScopeList.add(BeanUtil.copyProperties(dataScope, DataScope.class));

                        map.put(code,dataScopeList);
                    }

                }
            });
        }

        Fc.requireNotNull(SpringUtil.getBean(RedisUtil.class),"未获取到RedisUtil").set(CacheNames.TOKEN_DATA_SCOPE_KEY+authInfo.getAccessToken(), map , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        List<String> list = SystemCache.getUrlsByRoleIds(authInfo.getUser().getRoleIds(),client.getClientCode());
        Fc.requireNotNull(SpringUtil.getBean(RedisUtil.class),"未获取到RedisUtil").set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(), list , authInfo.getExpiresIn(), TimeUnit.SECONDS);
    }
}
