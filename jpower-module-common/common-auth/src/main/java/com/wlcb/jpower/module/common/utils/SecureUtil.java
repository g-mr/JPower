package com.wlcb.jpower.module.common.utils;


import cn.hutool.core.codec.Base64Decoder;
import com.wlcb.jpower.module.common.auth.RoleConstant;
import com.wlcb.jpower.module.common.auth.SecureConstant;
import com.wlcb.jpower.module.common.auth.TokenInfo;
import com.wlcb.jpower.module.common.auth.UserInfo;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.constants.CharPool;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.*;

import static com.wlcb.jpower.module.common.auth.SecureConstant.BASIC_HEADER_PREFIX;

/**
 * @Author mr.g
 * @Date 17:00 2020-08-24
 **/
@Slf4j
public class SecureUtil {
    private static final String REQUEST_JPOWER_USER = "REQUEST_JPOWER_USER";
    private final static String HEADER = TokenConstant.HEADER;
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
            user.setClientCode(Fc.toStr(claims.get(TokenConstant.CLIENT_CODE)));
            user.setUserId(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getUserId))));
            user.setTenantCode(Fc.toStr(claims.get(ClassUtil.getFiledName(UserInfo::getTenantCode))));
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
        return Fc.contains(getUserRole(), RoleConstant.ROOT_ID);
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
     * 获取用户账号
     *
     * @return userAccount
     */
    public static String getUserAccount() {
        UserInfo user = getUser();
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
     * 获取用角色
     *
     * @return userName
     */
    public static List<String> getUserRole() {
        UserInfo user = getUser();
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
     * 获取Claims
     *
     * @param request request
     * @return Claims
     */
    public static Claims getClaims(HttpServletRequest request) {
        return JwtUtil.parseJWT(JwtUtil.getToken(request));
    }

    /**
     * 创建令牌
     *
     * @param param      param
     * @param expire     expire
     * @return jwt
     */
    public static TokenInfo createJWT(Map<String, Object> param, long expire) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        byte[] apiKeySecretBytes = Base64.getDecoder().decode(BASE64_SECURITY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("Type", "JsonWebToken")
                .signWith(signatureAlgorithm, signingKey);

        //设置JWT参数
        param.forEach(builder::claim);

        Date exp = new Date(nowMillis + expire * 1000);
        builder.setExpiration(exp).setNotBefore(now);

        // 组装Token信息
        TokenInfo tokenInfo = new TokenInfo();
        tokenInfo.setToken(builder.compact());
        tokenInfo.setExpire(expire);

        return tokenInfo;
    }

    /**
     * 客户端信息解码
     */
    public static String[] getClientInfo() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, BASIC_HEADER_PREFIX);
        if (!header.startsWith(BASIC_HEADER_PREFIX)) {
            throw new RuntimeException("请求标头中没有客户端信息");
        }

        String decodeBasic = StringUtil.subAfter(header,BASIC_HEADER_PREFIX,false);
        return extractClient(decodeBasic);
    }

    private static String[] extractClient(String decodeBasic) {
        String token = base64Decoder(decodeBasic);
        if (StringUtil.contains(token, CharPool.COLON)) {
            return new String[]{
                    StringUtil.subBefore(token,StringPool.COLON,false),
                    StringUtil.subAfter(token,StringPool.COLON,false)};
        } else {
            throw new RuntimeException("无效的基本身份验证令牌");
        }
    }

    @SneakyThrows
    public static String base64Decoder(String header) {
        return new String(Base64Decoder.decode(header.getBytes(CharsetKit.UTF_8)), CharsetKit.UTF_8);
    }

    /**
     * 获取请求头中的客户端id
     */
    public static String getClientCodeFromHeader() {
        String[] tokens = getClientInfo();
        assert tokens.length == 2;
        return tokens[0];
    }

}
