package com.wlcb.jpower.gateway.utils;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;
import com.wlcb.jpower.module.common.utils.constants.TokenConstant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * @ClassName TokenUtil
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/30 0030 23:38
 * @Version 1.0
 */
public class TokenUtil {

    public static String getToken(ServerHttpRequest request) {
        String header = request.getHeaders().getFirst(TokenConstant.HEADER);

        HttpCookie httpCookie = request.getCookies().getFirst(TokenConstant.HEADER);
        String cookies = Fc.isNull(httpCookie)?null:httpCookie.getValue();

        String param = request.getQueryParams().getFirst(TokenConstant.HEADER);

        if (StringUtils.isAllBlank(header,cookies,param)){
            return null;
        }

        String token = Fc.isBlank(header)?Fc.isBlank(cookies)?param:cookies:header;
        if (StringUtil.isNotBlank(token) && token.length() > TokenConstant.AUTH_LENGTH) {
            String headStr = token.substring(0, 6).toLowerCase();
            if (headStr.compareTo(TokenConstant.JPOWER) == 0) {
                token = token.substring(TokenConstant.AUTH_LENGTH);
                return token;
            }
        }
        return null;
    }

}
