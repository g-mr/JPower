package com.wlcb.jpower.module.common.utils;


import com.wlcb.jpower.module.common.auth.*;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.netty.handler.codec.http.FullHttpRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * @Author mr.g
 * @Date 17:00 2020-08-24
 **/
@Slf4j
public class SecureUtil {
    private static final String REQUEST_JPOWER_USER = "REQUEST_JPOWER_USER";
    private final static String HEADER = TokenConstant.HEADER;
    private final static String CLIENT_CODE = ClassUtil.getFiledName(UserInfo::getClientCode);
    private static final String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(CharsetKit.CHARSET_UTF_8));
    private static final boolean TENANT_MODE = EnvBeanUtil.get("jpower.tenant.enable", Boolean.class, true);

    /**
     * 获取用户信息
     *
     * @return UserInfo
     */
    public static UserInfo getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (Fc.isNull(request)) {
            return null;
        }

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(REQUEST_JPOWER_USER);
        if (Fc.isNull(userInfo)) {
            userInfo = getUser(request);
            request.getSession().setAttribute(REQUEST_JPOWER_USER, userInfo);
        }

        return userInfo;
    }

    public static UserInfo getUser(Claims claims) {
        UserInfo user = new UserInfo();
        if (Fc.notNull(claims)) {
            user.setClientCode(Fc.toStr(claims.get(SecureUtil.CLIENT_CODE)));
            user.setUserId(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getUserId))));
            user.setTenantCode(Fc.toStr(ClassUtil.getFiledName(UserInfo::getTenantCode)));
            user.setLoginId(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getLoginId))));
            user.setRoleIds((List<String>) claims.get(ClassUtil.getFiledName(UserInfo::getRoleIds)));
            user.setUserName(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getUserName))));
            user.setTelephone(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getTelephone))));
            user.setUserType(Fc.toInt(claims.get(ClassUtil.getFiledName(UserInfo::getUserType))));
            user.setOrgId(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getOrgId))));
            user.setIsSysUser(Fc.toInt(claims.get(ClassUtil.getFiledName(UserInfo::getIsSysUser))));
        }
        return user.isEmpty()?null:user;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return UserInfo
     */
    public static UserInfo getUser(HttpServletRequest request) {
        return getUser(getClaims(request));
    }

    /**
     * 是否为超管
     *
     * @return boolean
     */
    public static boolean isRoot() {
        return Fc.contains(getUserRole().iterator(), RoleConstant.ROOT_ID);
    }

    /**
     * 获取用户id
     *
     * @return userId
     */
    public static String getUserId() {
        UserInfo user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getUserId();
    }

    /**
     * 获取部门id
     *
     * @return orgId
     */
    public static String getOrgId() {
        UserInfo user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getOrgId();
    }

    /**
     * 获取用户id
     *
     * @param request request
     * @return userId
     */
    public static String getUserId(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getUserId();
    }

    /**
     * 获取用户账号
     *
     * @return userAccount
     */
    public static String getUserAccount() {
        UserInfo user = getUser();
        return Fc.isNull(user) ? StringPool.EMPTY : user.getLoginId();
    }

    /**
     * 获取用户账号
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return Fc.isNull(user) ? StringPool.EMPTY : user.getLoginId();
    }

    /**
     * 获取用户名
     *
     * @return userName
     */
    public static String getUserName() {
        UserInfo user = getUser();
        return Fc.isNull(user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @param request request
     * @return userName
     */
    public static String getUserName(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return Fc.isNull(user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户角色
     *
     * @return userName
     */
    public static List<String> getUserRole() {
        return getUserRole(WebUtil.getRequest());
    }

    /**
     * 获取用角色
     *
     * @param request request
     * @return userName
     */
    public static List<String> getUserRole(HttpServletRequest request) {
        UserInfo user = getUser(request);
        if (Fc.isNull(user)) {
            List<String> list = new ArrayList<>();
            list.add(RoleConstant.ANONYMOUS_ID);
            return list;
        }
        return user.getRoleIds();
    }

    /**
     * 获取租户ID
     *
     * @return tenantId
     */
    public static String getTenantCode() {
        return getTenantCode(WebUtil.getRequest());
    }

    /**
     * 获取租户ID
     *
     * @param request request
     * @return tenantId
     */
    public static String getTenantCode(HttpServletRequest request) {
        if (!TENANT_MODE) {
            return StringPool.EMPTY;
        }
        UserInfo user = getUser(request);

        String tenantCode = StringPool.EMPTY;
        if (Fc.notNull(request)) {
            String code = request.getParameter(TokenConstant.TENANT_CODE);
            tenantCode = Fc.isBlank(code) ? request.getHeader(TokenConstant.HEADER_TENANT) : code;
        }

        //先从当前登陆用户中获取租户编码，如果当前用户没有登陆则去参数里获取租户编码如果还没有则去header里去取
        return Fc.isNull(user) ? Fc.isNull(request) ? StringPool.EMPTY : tenantCode : user.getTenantCode();
    }

    /**
     * 获取客户端id
     *
     * @return tenantId
     */
    public static String getClientCode() {
        UserInfo user = getUser();
        return Fc.isNull(user) ? StringPool.EMPTY : user.getClientCode();
    }

    /**
     * 获取客户端id
     *
     * @param request request
     * @return tenantId
     */
    public static String getClientCode(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return Fc.isNull(user) ? StringPool.EMPTY : user.getClientCode();
    }

    /**
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static Claims getClaims(HttpServletRequest request) {
        return JwtUtil.parseJWT(JwtUtil.getToken(request));
    }

    /**
     * 获取Claims
     *
     * @param fullHttpRequest
     * @return Claims
     */
    public static Claims getClaims(FullHttpRequest fullHttpRequest) {
        return JwtUtil.parseJWT(JwtUtil.getToken(fullHttpRequest));
    }

    /**
     * 获取请求头
     *
     * @return header
     */
    public static String getHeader() {
        return getHeader(Objects.requireNonNull(WebUtil.getRequest()));
    }

    /**
     * 获取请求头
     *
     * @param request request
     * @return header
     */
    public static String getHeader(HttpServletRequest request) {
        return request.getHeader(HEADER);
    }

    /**
     * 创建令牌
     *
     * @param user      user
     * @param audience  audience
     * @param issuer    issuer
     * @param tokenType tokenType
     * @return jwt
     */
    public static TokenInfo createJWT(Map<String, Object> user, String audience, String issuer, String tokenType, ClientDetails clientDetails) {

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        //生成密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("Type", "JsonWebToken")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);

        //设置JWT参数
        user.forEach(builder::claim);

        //设置应用Code
        builder.claim(CLIENT_CODE, clientDetails.getClientCode());

        //添加Token过期时间 默认过期是一天
        long expireMillis = DateUtil.getDate(1).getTime() - System.currentTimeMillis();
        if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
            expireMillis = clientDetails.getAccessTokenValidity() * 1000;
        } else if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
            expireMillis = clientDetails.getRefreshTokenValidity() * 1000;
        }
        long expMillis = nowMillis + expireMillis;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp).setNotBefore(now);

        // 组装Token信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire((int) expireMillis / 1000);

        return tokenInfo;
    }

    /**
     * 客户端信息解码
     */
    @SneakyThrows
    public static String[] extractAndDecodeHeader() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
        if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
            throw new RuntimeException("请求标头中没有客户端信息");
        }
        byte[] base64Token = header.substring(6).getBytes(CharsetKit.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64Util.decode(base64Token);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("无法解析Authentication");
        }

        String token = new String(decoded, CharsetKit.UTF_8);
        int index = token.indexOf(StringPool.COLON);
        if (index == -1) {
            throw new RuntimeException("无效的Authentication");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取请求头中的客户端id
     */
    public static String getClientCodeFromHeader() {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 校验Client
     *
     * @param clientCode   客户端code
     * @param clientSecret 客户端密钥
     * @return boolean
     */
    public static boolean validateClient(ClientDetails clientDetails, String clientCode, String clientSecret) {
        if (clientDetails != null) {
            return StringUtil.equals(clientCode, clientDetails.getClientCode()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
        }
        return false;
    }

}
