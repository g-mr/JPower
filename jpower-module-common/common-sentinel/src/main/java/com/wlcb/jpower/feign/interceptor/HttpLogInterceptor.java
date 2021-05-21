package com.wlcb.jpower.feign.interceptor;

import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @ClassName HeaderRequestInterceptor
 * @Description TODO feign调用时传递header
 * @Author 郭丁志
 * @Date 2020/9/13 0013 17:43
 * @Version 1.0
 */
@Slf4j
public class HttpLogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) {
        return null;
    }
}
