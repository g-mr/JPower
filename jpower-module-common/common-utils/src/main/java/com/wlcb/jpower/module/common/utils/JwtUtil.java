package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import io.jsonwebtoken.*;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import static com.wlcb.jpower.module.common.utils.constants.TokenConstant.JWT_BASE64_SECURITY;

/**
 * JWT TOKEN 工具
 *
 * @author mr.g
 */
public class JwtUtil {

    /**
     * token前缀长度
     **/
    public static Integer AUTH_LENGTH = TokenConstant.JPOWER.length()+1;

    /**
     * 解析令牌
     *
     * @author mr.g
     * @param token 令牌
     * @return 令牌解析结果
     */
    public static Claims parseJwt(@NonNull String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(JWT_BASE64_SECURITY)
                    .parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    /**
     * 解析获取最后的令牌
     *
     * @author mr.g
     * @param authToken 令牌
     * @return 去掉前缀后的令牌
     **/
    public static String parsingToken(@NonNull String authToken) {
        if (StringUtil.isNotBlank(authToken) && authToken.length() > AUTH_LENGTH && authToken.startsWith(TokenConstant.JPOWER)) {
            authToken = authToken.substring(AUTH_LENGTH);
            return authToken;
        }
        return StringPool.EMPTY;
    }

    /**
     * 从Web中获取令牌
     *
     * @author mr.g
     * @param request ServletRequest
     * @return 令牌
     */
    public static String getToken(HttpServletRequest request) {
        if (Fc.isNull(request)) {
            return null;
        }

        String auth = request.getHeader(TokenConstant.HEADER);
        if (StringUtil.isNotBlank(auth)) {
            return parsingToken(auth);
        }

        Cookie[] cookies = request.getCookies();
        if (Fc.isNotEmpty(cookies)) {
            Cookie cookie = Arrays.stream(cookies).filter(c -> StringUtils.equals(c.getName(), TokenConstant.HEADER) && Fc.isNotBlank(c.getValue())).findFirst().orElse(null);
            if (Fc.notNull(cookie)){
                return parsingToken(Fc.decode(cookie.getValue()));
            }
        }

        String parameter = request.getParameter(TokenConstant.HEADER);
        if (StringUtil.isNotBlank(parameter)) {
            return Fc.decode(parameter);
        }

        return StringPool.EMPTY;
    }

    /**
     * 创建令牌
     *
     * @author mr.g
     * @param param 令牌参数
     * @param expire 过期时间
     * @return 令牌
     */
    public static String createJwt(Map<String, Object> param, long expire) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //生成签名密钥
        Key signingKey = new SecretKeySpec(JWT_BASE64_SECURITY, signatureAlgorithm.getJcaName());

        //添加构成JWT的类
        JwtBuilder builder = Jwts.builder().setHeaderParam("Type", "JsonWebToken")
                .signWith(signatureAlgorithm, signingKey);

        //设置JWT参数
        param.forEach(builder::claim);

        Date exp = new Date(nowMillis + expire * 1000);
        builder.setExpiration(exp).setNotBefore(now);

        return builder.compact();
    }

}
