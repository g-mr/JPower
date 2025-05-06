package top.jpower.core.auth.utils;


import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.collection.ListUtil;
import cn.hutool.core.date.DateUnit;
import io.jsonwebtoken.Claims;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.auth.utils.constant.RoleConstant;
import top.jpower.core.auth.utils.constant.SecureConstant;
import top.jpower.core.auth.dto.UserInfo;
import top.jpower.core.util.constants.*;
import top.jpower.core.util.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Objects;

/**
 * 系统鉴权信息获取工具
 *
 * @author mr.g
 **/
@Slf4j
public class ShieldUtil {

    private static final String REQUEST_USER_SESSION = "request_user_session";
    private static final String REQUEST_USER_TOKEN = "request_user_token";

    /**
     * 获取用户信息
     *
     * @return UserInfo
     */
    public static UserInfo getUser() {
        HttpServletRequest request = WebUtil.getRequest();
        String token = JwtUtil.getToken(request);
        if (Fc.isNull(request) || Fc.isBlank(token)) {
            return null;
        }

        UserInfo userInfo = (UserInfo) request.getSession().getAttribute(REQUEST_USER_SESSION);
        String sessionToken = (String) request.getSession().getAttribute(REQUEST_USER_TOKEN);
        if (Fc.isNull(userInfo) || !Fc.equalsValue(sessionToken,token)) {
            userInfo = getUser(request);
            if (Fc.notNull(userInfo)){
                long bet = DateUtil.between(DateUtil.date(),getClaims(request).getExpiration(), DateUnit.SECOND, false);
                if (bet > 0){
                    request.getSession().setMaxInactiveInterval((int) bet);
                    request.getSession().setAttribute(REQUEST_USER_SESSION, userInfo);
                    request.getSession().setAttribute(REQUEST_USER_TOKEN, token);
                }
            }
        }

        return userInfo;
    }

    public static UserInfo getUser(Claims claims) {
        UserInfo user = new UserInfo();
        if (Fc.notNull(claims)) {
            user = BeanUtil.toBean(claims, UserInfo.class);
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
    public static Long getUserId() {
        UserInfo user = getUser();
        return (null == user) ? null : user.getUserId();
    }

    /**
     * 获取部门id
     *
     * @return orgId
     */
    public static Long getOrgId() {
        UserInfo user = getUser();
        return (null == user) ? null : user.getOrgId();
    }

    /**
     * 获取用户账号
     *
     * @return userAccount
     */
    public static String getLoginId() {
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
     * 获取用户角色
     *
     * @return userName
     */
    public static List<Long> getUserRole() {
        UserInfo user = getUser();
        if (Fc.isNull(user)) {
            return ListUtil.of(RoleConstant.ANONYMOUS_ID);
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
        if (Fc.isNull(request)){
            log.warn("HttpServletRequest为空，无法获取当前租户");
            return StringPool.EMPTY;
        }

        UserInfo user = getUser(request);

        //先从当前登陆用户中获取租户编码，如果当前用户没有登陆则去参数里获取租户编码如果还没有则去header里去取
        if (Fc.isNull(user)){
            String tenantCode = StringPool.EMPTY;
            if (Fc.notNull(request)) {
                String code = request.getParameter(TokenConstant.TENANT_CODE);
                tenantCode = Fc.isBlank(code) ? request.getHeader(JpowerConstants.HEADER_TENANT) : code;
            }
            return tenantCode;
        }else {
            return user.getTenantCode();
        }
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
        return JwtUtil.parseJwt(JwtUtil.getToken(request));
    }

    /**
     * 客户端信息解码
     */
    public static String[] getClientInfo() {
        // 获取请求头客户端信息
        String header = Objects.requireNonNull(WebUtil.getRequest()).getHeader(SecureConstant.BASIC_HEADER_KEY);
        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, SecureConstant.BASIC_HEADER_PREFIX);
        if (!header.startsWith(SecureConstant.BASIC_HEADER_PREFIX)) {
            throw new IllegalArgumentException("请求头中没有客户端信息");
        }

        String decodeBasic = StringUtil.subAfter(header, SecureConstant.BASIC_HEADER_PREFIX,false);
        return extractClient(decodeBasic);
    }

    @SneakyThrows(UnsupportedEncodingException.class)
    public static String[] extractClient(String decodeBasic) {
        String token = new String(Base64Decoder.decode(decodeBasic.getBytes(CharsetKit.UTF_8)), CharsetKit.UTF_8);
        if (StringUtil.contains(token, CharPool.COLON) && !StringUtil.startWith(token,CharPool.COLON) && !StringUtil.endWith(token,CharPool.COLON)) {
            String[] clients = new String[]{
                    StringUtil.subBefore(token,StringPool.COLON,false),
                    StringUtil.subAfter(token,StringPool.COLON,false)};
            return clients;
        } else {
            throw new IllegalArgumentException("无效的基本身份验证令牌");
        }
    }

    /**
     * 获取请求头中的客户端Code
     */
    public static String getClientCodeFromHeader() {
        String[] tokens = getClientInfo();
        assert tokens.length == 2;
        return tokens[0];
    }

    /**
     * 获取请求头中的客户端密钥
     */
    public static String getClientSecretFromHeader() {
        String[] tokens = getClientInfo();
        assert tokens.length == 2;
        return tokens[1];
    }

}
