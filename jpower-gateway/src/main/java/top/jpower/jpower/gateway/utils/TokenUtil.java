package top.jpower.jpower.gateway.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.auth.utils.ShieldUtil;
import top.jpower.core.auth.utils.constant.SecureConstant;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

import static top.jpower.core.auth.utils.constant.SecureConstant.BASIC_HEADER_PREFIX;

/**
 * Token工具
 *
 * @author mr.g
 */
public class TokenUtil {

    /**
     * 获取token
     *
     * @author mr.g
     * @param request 请求
     * @return java.lang.String
     **/
    public static String getToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(JpowerConstants.AUTH_HEADER);

        AuthProperties properties = SpringUtil.getBean(AuthProperties.class);

        // 打开cookie就去验证cookie
        String cookies = StringPool.EMPTY;
        if (Fc.notNull(properties) && properties.getCookie()){
            HttpCookie httpCookie = request.getCookies().getFirst(JpowerConstants.AUTH_HEADER);
            cookies = Fc.isNull(httpCookie)?null:httpCookie.getValue();
        }

        if (StrUtil.isAllBlank(header,cookies)){
            String param = request.getQueryParams().getFirst(JpowerConstants.AUTH_HEADER);
            if (StringUtil.isNotBlank(param)) {
                return param;
            }

            return null;
        }

        return Fc.isBlank(header) ? cookies : JwtUtil.parsingToken(header);
    }


    public static String getClientCodeFromHeader(ServerHttpRequest request) {
        // 获取请求头客户端信息
        String header = Fc.requireNotNull(request,"未获取到Request").getHeaders().getFirst(SecureConstant.BASIC_HEADER_KEY);
        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, BASIC_HEADER_PREFIX);
        if (!header.startsWith(BASIC_HEADER_PREFIX)) {
            throw new IllegalArgumentException("请求头中没有客户端信息");
        }

        String decodeBasic = StringUtil.subAfter(header, BASIC_HEADER_PREFIX, false);
        String[] tokens = ShieldUtil.extractClient(decodeBasic);
        assert tokens.length == 2;
        return tokens[0];
    }
}
