package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.properties.AuthInfoConfiguration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
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
            if (Fc.isBlank(authInfo.getUser())){
                throw new NullPointerException("header ==> name is null");
            }

            log.info("--> TEST REST AUTH {} {}",authInfo.getUser(),authInfo.getPassword());

            return chain.proceed(chain.request().newBuilder().addHeader(authInfo.getUser(),authInfo.getPassword()).build());
        }

        return chain.proceed(chain.request());
    }
}
