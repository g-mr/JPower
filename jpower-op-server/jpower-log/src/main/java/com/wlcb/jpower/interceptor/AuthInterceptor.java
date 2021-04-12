package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.properties.AuthInfoConfiguration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@Slf4j
@AllArgsConstructor
public final class AuthInterceptor implements Interceptor {

    private final AuthInfoConfiguration authInfo;

    @Override
    @SneakyThrows
    public Response intercept(Chain chain) {
        if (!Fc.isNull(authInfo)){
            Request request = chain.request();
            String token = getToken(request);
            log.info("--> TEST REST AUTH {} {}",authInfo.getTokenName(),token);
            return chain.proceed(request.newBuilder().addHeader(authInfo.getTokenName(),token).build());
        }

        return chain.proceed(chain.request());
    }

    /**
     * 获取token
     * @Author mr.g
     * @param request
     * @return java.lang.String
     **/
    private String getToken(Request request) {
        String domain = "http://"+request.url().host()+StringPool.COLON+request.url().port();

        String token = authInfo.getToken();
        if (Fc.isBlank(token)){
            token = requestToken(domain);
        }

        return spliceToken(token);
    }

    /**
     * 拼接token
     * @Author mr.g
     * @param token
     * @return java.lang.String
     **/
    private String spliceToken(String token) {
        if (Fc.isNotBlank(authInfo.getTokenPrefix())){
            token = authInfo.getTokenPrefix() + authInfo.getTokenDelimiter() + token;
        }

        if (Fc.isNotBlank(authInfo.getTokenSuffix())){
            token = token + authInfo.getTokenDelimiter() + authInfo.getTokenSuffix();
        }
        return token;
    }

    /**
     * 请求token
     * @Author mr.g
     * @param domain
     * @return java.lang.String
     **/
    private String requestToken(String domain) {
        return null;
    }
}
