package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@AllArgsConstructor
public final class AuthInterceptor implements Interceptor {

    private final String user;
    private final String password;

    @Override
    @SneakyThrows
    public Response intercept(Chain chain) {
        if (Fc.isBlank(user)){
            throw new NullPointerException("header ==> name is null");
        }
        Request request = chain.request();
        request = request.newBuilder().addHeader(user,password).build();
        return chain.proceed(request);
    }
}
