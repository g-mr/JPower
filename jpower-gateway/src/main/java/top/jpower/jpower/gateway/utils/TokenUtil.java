package top.jpower.jpower.gateway.utils;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import top.jpower.core.auth.properties.AuthProperties;
import top.jpower.core.auth.utils.JwtUtil;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;

/**
 * @ClassName TokenUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 23:38
 * @Version 1.0
 */
public class TokenUtil {

    /**
     * 获取token
     *
     * @author mr.g
     * @param request
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

        if (StringUtils.isAllBlank(header,cookies)){
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
//        String header = Fc.requireNotNull(request,"未获取到Request").getHeaders().getFirst(SecureConstant.BASIC_HEADER_KEY);
//        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, BASIC_HEADER_PREFIX);
//        if (!header.startsWith(BASIC_HEADER_PREFIX)) {
//            throw new IllegalArgumentException("请求头中没有客户端信息");
//        }
//
//        String decodeBasic = StringUtil.subAfter(header,BASIC_HEADER_PREFIX,false);
//        String[] tokens = ShieldUtil.extractClient(decodeBasic);
//        assert tokens.length == 2;
//        return tokens[0];
        return "admin";
    }
}
