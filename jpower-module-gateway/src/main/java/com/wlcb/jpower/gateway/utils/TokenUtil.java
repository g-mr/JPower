package com.wlcb.jpower.gateway.utils;

import cn.hutool.core.util.URLUtil;
import com.wlcb.jpower.module.common.auth.SecureConstant;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JwtUtil;
import com.wlcb.jpower.module.common.utils.ShieldUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;

import static com.wlcb.jpower.module.common.auth.SecureConstant.BASIC_HEADER_PREFIX;

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
        String header = request.getHeaders().getFirst(TokenConstant.HEADER);

        HttpCookie httpCookie = request.getCookies().getFirst(TokenConstant.HEADER);
        String cookies = Fc.isNull(httpCookie)?null:httpCookie.getValue();

        if (StringUtils.isAllBlank(header,cookies)){
            String param = request.getQueryParams().getFirst(TokenConstant.HEADER);
            if (StringUtil.isNotBlank(param)) {
                return param;
            }

            return null;
        }

        String token = Fc.isBlank(header)? URLUtil.decode(cookies):header;
        if (StringUtil.isNotBlank(token)) {
            return JwtUtil.parsingToken(token);
        }


        return null;
    }


    public static String getClientCodeFromHeader(ServerHttpRequest request) {
        // 获取请求头客户端信息
        String header = Fc.requireNotNull(request,"未获取到Request").getHeaders().getFirst(SecureConstant.BASIC_HEADER_KEY);
        header = Fc.toStr(header).replace(SecureConstant.BASIC_HEADER_PREFIX_EXT, BASIC_HEADER_PREFIX);
        if (!header.startsWith(BASIC_HEADER_PREFIX)) {
            throw new IllegalArgumentException("请求头中没有客户端信息");
        }

        String decodeBasic = StringUtil.subAfter(header,BASIC_HEADER_PREFIX,false);
        String[] tokens = ShieldUtil.extractClient(decodeBasic);
        assert tokens.length == 2;
        return tokens[0];
    }
}
