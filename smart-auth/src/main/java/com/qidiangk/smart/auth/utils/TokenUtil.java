package com.qidiangk.smart.auth.utils;

import com.qidiangk.smart.common.constants.CacheNames;
import com.qidiangk.smart.common.enums.YN01Enum;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.dbs.datascope.DataScope;
import top.jpower.core.exception.throwable.BusinessException;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.constants.TokenConstant;
import top.jpower.core.util.utils.*;
import com.qidiangk.smart.auth.dto.AuthInfo;
import com.qidiangk.smart.system.api.cache.SystemCache;
import com.qidiangk.smart.system.api.dto.ClientDTO;
import com.qidiangk.smart.system.api.dto.DataScopeDTO;
import com.qidiangk.smart.system.api.dto.FunctionDTO;
import com.qidiangk.smart.user.api.dto.CoreUserDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.qidiangk.smart.common.constants.CacheNames.TOKEN_USER_KEY;


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
    public final static String TOKEN_EXPIRED = "令牌已过期，请重新登录";
    public final static String USER_NOT_ACTIVATION = "用户尚未激活";


    private static final RedisService redisService;
    private static final AuthProperties AUTH_PROPERTIES;

    static {
        redisService = SpringUtil.getBean(RedisService.class);
        AUTH_PROPERTIES = SpringUtil.getBean(AuthProperties.class);
    }

    /**
     * 获取客户端信息
     * @return 客户端信息
     */
    public static ClientDTO getClientDetails(){
        String[] tokens = ShieldUtil.getClientInfo();
        assert tokens.length == 2;
        String clientCode = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
		ClientDTO client = SystemCache.getClientByClientCode(clientCode);

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
    public static boolean validateClient(ClientDTO client, String clientCode, String clientSecret) {
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
     * @param user 用户信息
     * @return token
     */
    public static AuthInfo createAuthInfo(CoreUserDTO user) {
		if (!user.getActivationStatus()) {
			throw new BusinessException(USER_NOT_ACTIVATION);
		}

		ClientDTO client = getClientDetails();
        assert client != null;

        //设置jwt参数
        Map<String, Object> param = Fc.toMap(user);
        param.put(TokenConstant.TOKEN_TYPE, TokenConstant.ACCESS_TOKEN);
        param.put(TokenConstant.CLIENT_CODE, client.getClientCode());

        //token过期时间
        long expire = getExpire(client.getAccessTokenValidity());

        AuthInfo authInfo = new AuthInfo();
        authInfo.setAccessToken(JwtUtil.createJwt(param, expire));
        authInfo.setExpiresIn(expire);
        authInfo.setRefreshToken(createRefreshToken(user, client));
        authInfo.setTokenType(TokenConstant.TOKEN_PREFIX);
        cacheAuth(authInfo, user, client);
        return authInfo;
    }

    /**
     * 创建refreshToken
     *
     * @param user 用户信息
     * @return refreshToken
     */
    private static String createRefreshToken(CoreUserDTO user,ClientDTO client) {
        return JwtUtil.createJwt(ChainMap.<String, Object>create()
                .put(TokenConstant.TOKEN_TYPE, TokenConstant.REFRESH_TOKEN)
                .put(TokenConstant.USER_ID, user.getId())
                .put(TokenConstant.CLIENT_CODE, client.getClientCode()).build()
                ,getExpire(client.getRefreshTokenValidity()));
    }

    /**
     * 缓存鉴权信息
     *
     * @author mr.g
     * @param authInfo 鉴权信息
     **/
    private static void cacheAuth(AuthInfo authInfo, CoreUserDTO user, ClientDTO client) {
        List<DataScopeDTO> dataScopeRoleList = SystemCache.getDataScopeByRole(user.getRoleIds(),client.getClientCode());
        List<FunctionDTO> menuList = SystemCache.getMenuListByRole(user.getRoleIds(),client.getClientCode());

        Map<String, List<DataScope>> map = ChainMap.<String,List<DataScope>>create().build();
        if (Fc.isNotEmpty(dataScopeRoleList)){
            dataScopeRoleList.forEach(dataScope -> {
                String code = Fc.isNotEmpty(menuList) ? menuList.stream().filter(menu -> Fc.equalsValue(menu.getId(),dataScope.getMenuId())).map(FunctionDTO::getCode).findFirst().orElse(null) : null;
                if (Fc.isNotBlank(code)){

                    boolean is = true;
                    //角色配置的数据权限比所有角色可执行的权限优先级要高，所以判断有自己的权限的时候就不要全角色执行的权限了
                    if (Fc.equalsValue(dataScope.getAllRole(), YN01Enum.Y.getValue())){
                        is = dataScopeRoleList.stream().noneMatch(scope-> Fc.equalsValue(scope.getAllRole(), YN01Enum.N.getValue()) && Fc.equalsValue(dataScope.getScopeClass(), scope.getScopeClass()));
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

        redisService.valueOps().set(CacheNames.TOKEN_DATA_SCOPE_KEY+authInfo.getAccessToken(), map , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        List<String> list = SystemCache.getUrlsByRoleIds(user.getRoleIds(),client.getClientCode());
        redisService.valueOps().set(CacheNames.TOKEN_URL_KEY+authInfo.getAccessToken(), list , authInfo.getExpiresIn(), TimeUnit.SECONDS);

        //缓存用户在线信息
        String oldToken = JwtUtil.getToken(WebUtil.getRequest());
        //如果有旧token代表的是刷新token
        if (Fc.isNotBlank(oldToken)){
            redisService.delete(TOKEN_USER_KEY+user.getId()+ StringPool.COLON+oldToken);
        }
        redisService.valueOps().set(TOKEN_USER_KEY+user.getId()+ StringPool.COLON+authInfo.getAccessToken(),ChainMap.<String,Object>create().put("client",client.getClientCode()).put("ip", WebUtil.getIp()).put("date", DateUtil.now()).build(),authInfo.getExpiresIn(), TimeUnit.SECONDS);

        // cookie
        if (AUTH_PROPERTIES.getCookie()){
            WebUtil.addCookie(WebUtil.getResponse(), JpowerConstants.AUTH_HEADER, authInfo.getAccessToken(), Fc.toInt(authInfo.getExpiresIn(), 0));
        }
    }
}
