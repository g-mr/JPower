package com.wlcb.jpower.gateway.utils;

import cn.hutool.core.util.URLUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.JwtUtil;
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

}
