package com.wlcb.jpower.module.common.utils;


import com.wlcb.jpower.module.base.exception.BusinessException;
import com.wlcb.jpower.module.common.auth.*;
import com.wlcb.jpower.module.common.service.core.user.CoreUserService;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

/**
 * Secure工具类
 *
 * @author Chill
 */
public class SecureUtil {
    private static final String JPOWER_USER_REQUEST_ATTR = "_JPOWER_USER_REQUEST_ATTR_";

    private final static String HEADER = TokenConstant.HEADER;
    private final static String BEARER = TokenConstant.JPOWER;

    private final static String ACCOUNT = TokenConstant.ACCOUNT;
    private final static String USER_ID = TokenConstant.USER_ID;
    private final static String USER_NAME = TokenConstant.USER_NAME;
//    private final static String TENANT_ID = TokenConstant.TENANT_ID;
    private final static String CLIENT_ID = TokenConstant.CLIENT_ID;
    private final static String TELE_PHONE = TokenConstant.TELE_PHONE;
    private final static String USER_TYPE = TokenConstant.USER_TYPE;
    private final static String ORG_ID = TokenConstant.ORG_ID;
    private final static String ROLE_IDS = TokenConstant.ROLE_IDS;
    private final static String IS_SYS_USER = TokenConstant.IS_SYS_USER;

    private final static Integer AUTH_LENGTH = TokenConstant.AUTH_LENGTH;

    private static String BASE64_SECURITY = Base64.getEncoder().encodeToString(TokenConstant.SIGN_KEY.getBytes(CharsetKit.CHARSET_UTF_8));

    // TODO: 2020-07-27 先放在这里不报错，后期要实现系统表替换成系统的service
    private static CoreUserService clientDetailsService;

    static {
        clientDetailsService = SpringUtil.getBean(CoreUserService.class);
    }

    /**
     * 获取用户信息
     *
     * @return BladeUser
     */
    public static UserInfo getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        if (request == null) {
            return null;
        }
        // 优先从 request 中获取
        Object coreuser = request.getAttribute(JPOWER_USER_REQUEST_ATTR);
        if (coreuser == null) {
            coreuser = getUser(request);
            if (coreuser != null) {
                // 设置到 request 中
                request.setAttribute(JPOWER_USER_REQUEST_ATTR, coreuser);
            }
        }
        return (UserInfo) coreuser;
    }

    /**
     * 获取用户信息
     *
     * @param request request
     * @return BladeUser
     */
    public static UserInfo getUser(HttpServletRequest request) {
        Claims claims = getClaims(request);

        return getUser(claims);
    }

    public static UserInfo getUser(Claims claims ) {
        if (claims == null) {
            return null;
        }

        String clientId = Fc.toStr(claims.get(SecureUtil.CLIENT_ID));
        String userId = Fc.toStr(claims.get(SecureUtil.USER_ID));
//        String tenantId = Fc.toStr(claims.get(SecureUtil.TENANT_ID));
        List<String> roleIds = (List<String>) claims.get(SecureUtil.ROLE_IDS);
        String account = Fc.toStr(claims.get(SecureUtil.ACCOUNT));
        String userName = Fc.toStr(claims.get(SecureUtil.USER_NAME));
        String telePhone = Fc.toStr(claims.get(SecureUtil.TELE_PHONE));
        Integer userType = Fc.toInt(claims.get(SecureUtil.USER_TYPE));
        String orgId = Fc.toStr(claims.get(SecureUtil.ORG_ID));
        Integer isSysUser = Fc.toInt(claims.get(SecureUtil.IS_SYS_USER));

        UserInfo coreUser = new UserInfo();
        coreUser.setClientId(clientId);
        coreUser.setUserId(userId);
//        coreUser.setTenantId(tenantId);
        coreUser.setLoginId(account);
        coreUser.setRoleIds(roleIds);
        coreUser.setUserName(userName);
        coreUser.setTelephone(telePhone);
        coreUser.setUserType(userType);
        coreUser.setOrgId(orgId);
        coreUser.setIsSysUser(isSysUser);
        return coreUser;
    }

    /**
     * 是否为超管
     *
     * @return boolean
     */
    public static boolean isAdministrator() {
        return Fc.contains(getUserRole().iterator(), RoleConstant.ADMIN_ID);
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
        return (null == user) ? StringPool.EMPTY : user.getLoginId();
    }

    /**
     * 获取用户账号
     *
     * @param request request
     * @return userAccount
     */
    public static String getUserAccount(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getLoginId();
    }

    /**
     * 获取用户名
     *
     * @return userName
     */
    public static String getUserName() {
        UserInfo user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户名
     *
     * @param request request
     * @return userName
     */
    public static String getUserName(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getUserName();
    }

    /**
     * 获取用户角色
     *
     * @return userName
     */
    public static List<String> getUserRole() {
        UserInfo user = getUser();
        return (null == user) ? new ArrayList<>() : user.getRoleIds();
    }

    /**
     * 获取用角色
     *
     * @param request request
     * @return userName
     */
    public static List<String> getUserRole(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return (null == user) ? new ArrayList<>() : user.getRoleIds();
    }

    /**
     * 获取租户ID
     *
     * @return tenantId
     */
//    public static String getTenantId() {
//        JpowerUser user = getUser();
//        return (null == user) ? StringPool.EMPTY : user.getTenantId();
//    }
//
//    /**
//     * 获取租户ID
//     *
//     * @param request request
//     * @return tenantId
//     */
//    public static String getTenantId(HttpServletRequest request) {
//        JpowerUser user = getUser(request);
//        return (null == user) ? StringPool.EMPTY : user.getTenantId();
//    }

    /**
     * 获取客户端id
     *
     * @return tenantId
     */
    public static String getClientId() {
        UserInfo user = getUser();
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取客户端id
     *
     * @param request request
     * @return tenantId
     */
    public static String getClientId(HttpServletRequest request) {
        UserInfo user = getUser(request);
        return (null == user) ? StringPool.EMPTY : user.getClientId();
    }

    /**
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static Claims getClaims(HttpServletRequest request) {
        return SecureUtil.parseJWT(JwtUtil.getToken(request));
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
     * 解析jsonWebToken
     *
     * @param jsonWebToken jsonWebToken
     * @return Claims
     */
    public static Claims parseJWT(String jsonWebToken) {
        if (Fc.isBlank(jsonWebToken)){
            return null;
        }

        try {
            return Jwts.parser()
                    .setSigningKey(Base64.getDecoder().decode(BASE64_SECURITY))
                    .parseClaimsJws(jsonWebToken).getBody();
        } catch (Exception ex) {
            return null;
        }
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
    public static TokenInfo createJWT(Map<String, Object> user, String audience, String issuer, String tokenType) {

        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        String clientId = tokens[0];
        String clientSecret = tokens[1];

        // 获取客户端信息
//        IClientDetails clientDetails = clientDetails(clientId);
        Map<String,String> clientDetails = clientDetails(clientId);

        // 校验客户端信息
        if (!validateClient(clientDetails, clientId, clientSecret)) {
            throw new BusinessException("客户端认证失败!");
        }

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("typ", "JsonWebToken")
                .setIssuer(issuer)
                .setAudience(audience)
                .signWith(signatureAlgorithm, signingKey);

        //设置JWT参数
        user.forEach(builder::claim);

        //设置应用id
        builder.claim(CLIENT_ID, clientId);

        //添加Token过期时间
        long expireMillis;
        if (tokenType.equals(TokenConstant.ACCESS_TOKEN)) {
            expireMillis = NumberUtil.toInt(clientDetails.get("accessTokenValidity")) * 1000;
        } else if (tokenType.equals(TokenConstant.REFRESH_TOKEN)) {
            expireMillis = NumberUtil.toInt(clientDetails.get("refreshTokenValidity")) * 1000;
        } else {
            expireMillis = getExpire();
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
     * 获取过期时间(次日凌晨3点)
     *
     * @return expire
     */
    public static long getExpire() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, 1);
        cal.set(Calendar.HOUR_OF_DAY, 3);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis() - System.currentTimeMillis();
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
            throw new BusinessException("No client information in request header");
        }
        byte[] base64Token = header.substring(6).getBytes(CharsetKit.UTF_8);

        byte[] decoded;
        try {
            decoded = Base64.getDecoder().decode(base64Token);
        } catch (IllegalArgumentException var7) {
            throw new RuntimeException("Failed to decode basic authentication token");
        }

        String token = new String(decoded, CharsetKit.UTF_8);
        int index = token.indexOf(StringPool.COLON);
        if (index == -1) {
            throw new RuntimeException("Invalid basic authentication token");
        } else {
            return new String[]{token.substring(0, index), token.substring(index + 1)};
        }
    }

    /**
     * 获取请求头中的客户端id
     */
    public static String getClientIdFromHeader() {
        String[] tokens = extractAndDecodeHeader();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 获取客户端信息
     *
     * @param clientId 客户端id
     * @return clientDetails
     */
//    private static IClientDetails clientDetails(String clientId) {
//        return clientDetailsService.loadClientByClientId(clientId);
//    }
    private static Map<String,String> clientDetails(String clientId) {
        //暂时先支持后台和微信，之后新增了system维护再加进来
        Map<String,String> map = new HashMap<>();
        if(StringUtil.equals(clientId,"wx")){
            map.put("clientId","wx");
            map.put("clientSecret","jpower_wx");
            map.put("accessTokenValidity","1800");
            map.put("refreshTokenValidity","2400");
            return map;
        }else if(StringUtil.equals(clientId,"admin")){
            map.put("clientId","admin");
            map.put("clientSecret","jpower_admin");
            map.put("accessTokenValidity","1800");
            map.put("refreshTokenValidity","2400");
            return map;
        }else {
            return null;
        }
    }

    /**
     * 校验Client
     *
     * @param clientId     客户端id
     * @param clientSecret 客户端密钥
     * @return boolean
     */
//    private static boolean validateClient(IClientDetails clientDetails, String clientId, String clientSecret) {
//        if (clientDetails != null) {
//            return StringUtil.equals(clientId, clientDetails.getClientId()) && StringUtil.equals(clientSecret, clientDetails.getClientSecret());
//        }
//        return false;
//    }
    private static boolean validateClient(Map<String,String> map, String clientId, String clientSecret) {
        //暂时先支持后台和微信，之后新增了system维护再加进来
        if (map != null) {
            return StringUtil.equals(clientId, map.get("clientId")) && StringUtil.equals(clientSecret, map.get("clientSecret"));
        }
        return false;
    }

}
