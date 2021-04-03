package com.wlcb.jpower.interceptor;

import lombok.AllArgsConstructor;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@AllArgsConstructor
public final class AuthInterceptor implements Interceptor {

    private final String user;
    private final String password;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        request = request.newBuilder().addHeader(user,password).build();
        return chain.proceed(request);
    }
}
