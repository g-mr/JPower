package com.qidiangk.smart.maxkb.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.github.lianjiatech.retrofit.spring.boot.exception.ReadResponseBodyException;
import com.github.lianjiatech.retrofit.spring.boot.interceptor.BasePathMatchInterceptor;
import com.github.lianjiatech.retrofit.spring.boot.util.RetrofitUtils;
import com.qidiangk.smart.maxkb.config.property.MaxKBProperty;
import com.qidiangk.smart.maxkb.service.ITokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import top.jpower.core.util.rsp.R;
import top.jpower.core.util.utils.JsonUtil;

import java.io.IOException;

@Component
@EnableConfigurationProperties(MaxKBProperty.class)
@RequiredArgsConstructor
@Slf4j
public class MaxKBTokenInterceptor extends BasePathMatchInterceptor {

    private static final String TOKEN_TYPE = "Bearer ";

    private final ITokenService tokenService;

    @Override
    public Response doIntercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request newReq = request.newBuilder()
                .addHeader("Authorization", TOKEN_TYPE + tokenService.getToken())
                .build();

        Response response = chain.proceed(newReq);

        if (response.isSuccessful() || response.code() == 401){
            try {
                String body = RetrofitUtils.readResponseBody(response);
                if (JsonUtil.isJsonObject(body)){
                    R r = JsonUtil.parseObject(body, R.class);
                    if (r.getCode() == 1002 || r.getCode() == 1003){
                        log.warn("MaxKB Token过期, 重新请求, body={}", body);

                        tokenService.removeToken();
                        return chain.proceed(request.newBuilder()
                                .addHeader("Authorization", tokenService.getToken())
                                .build());
                    }
                }
            } catch (ReadResponseBodyException e){
                log.error("[{}]请求结果body转换异常==>>{}", request.url(), ExceptionUtil.stacktraceToString(e));
            }
        }

        return response;
    }

}