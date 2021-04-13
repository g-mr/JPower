package com.wlcb.jpower.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Splitter;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.properties.AuthInfoConfiguration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.commons.lang.NullArgumentException;
import org.apache.http.HttpException;
import org.jetbrains.annotations.NotNull;
import sun.plugin.dom.exception.InvalidStateException;

import java.util.Map;

/**
 * 接口监控鉴权拦截器
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@Slf4j
@AllArgsConstructor
public final class AuthInterceptor implements Interceptor {

    private static final String CACHE_NAME = "RESTFUL-MONITOR-AUTH";

    private final AuthInfoConfiguration authInfo;

    /**
     * 获取token一旦报错，整个服务得本次监控将取消
     * @author mr.g
     * @param chain
     * @return okhttp3.Response
     */
    @Override
    @SneakyThrows
    public Response intercept(@NotNull Chain chain) {
        if (!Fc.isNull(authInfo)){
            Request request = chain.request();
            String token = getToken(request);
            log.info("--> TEST REST AUTH {} {}",authInfo.getTokenName(),token);

            switch (authInfo.getTokenPosition()){
                case HEADER:
                    return chain.proceed(request.newBuilder().addHeader(authInfo.getTokenName(),token).build());
                case FORM:
                    // TODO: 2021/4/13 0013 这里需要测试FormBody是否可以正确获取，以及测试无参数得情况下是否正常
                    if (request.body() instanceof FormBody) {
                        // 构造新的请求表单
                        FormBody.Builder builder = new FormBody.Builder();
                        FormBody body = (FormBody) request.body();
                        //将以前的参数添加
                        for (int i = 0; i < body.size(); i++) {
                            builder.add(body.encodedName(i), body.encodedValue(i));
                        }
                        //追加token参数
                        builder.add(authInfo.getTokenName(), token);
                        request = request.newBuilder().post(builder.build()).build();
                        return chain.proceed(request);
                    }
                case QUERY:
                    // TODO: 2021/4/13 0013 这里需要测试post请求是否正常，以及get请求有参（加上是否以前得参数没有了）无参得情况下是否都能加上
                    HttpUrl url = request.url();
                    HttpUrl newUrl = url.newBuilder()
                            .addEncodedQueryParameter(authInfo.getTokenName(),token)
                            .build();
                    return chain.proceed(request.newBuilder().url(newUrl).build());
                default:
                    throw new InvalidStateException("auth request tokenPosition error tokenPosition=>"+authInfo.getTokenPosition());
            }
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
        // TODO: 2021/4/13 0013 这里需要测试真实域名得获取情况
        String domain = "http://"+request.url().host()+StringPool.COLON+request.url().port();

        String token = authInfo.getToken();
        if (Fc.isBlank(token)){
            CacheMap<String,String> cacheMap = CacheMap.getInstance(CACHE_NAME);
            if (Fc.isNull(cacheMap.get(domain))){
                token = requestToken(domain);
                cacheMap.put(domain,token,authInfo.getExpiresIn(),authInfo.getExpiresUnit());
            }else {
                token = cacheMap.get(domain);
            }
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
    @SneakyThrows
    private String requestToken(String domain) throws NullArgumentException {

        String httpUrl = authInfo.getUrl().startsWith(StringPool.SLASH)?domain.concat(authInfo.getUrl()):domain.concat(StringPool.SLASH+authInfo.getUrl());

        Map<String, String> headers = Fc.isBlank(authInfo.getHeaders())?null:Splitter.on(StringPool.AMPERSAND).withKeyValueSeparator(StringPool.EQUALS).split(authInfo.getHeaders());
        Map<String, String> forms = Fc.isBlank(authInfo.getParams())?null:Splitter.on(StringPool.AMPERSAND).withKeyValueSeparator(StringPool.EQUALS).split(authInfo.getParams());

        OkHttp okHttp;
        switch (authInfo.getMethod()) {
            case HEAD :
                okHttp = OkHttp.head(httpUrl,headers,forms).execute();
                break;
            case GET :
                okHttp = OkHttp.get(httpUrl,headers,forms).execute();
                break;
            case POST :
                okHttp = OkHttp.post(httpUrl,headers,forms).execute();
                break;
            default:
                throw new IllegalStateException("unsupported request methodType");
        }
        if (Fc.isNull(okHttp.getResponse())){
            throw new HttpException("auth request error==>"+okHttp.getError());
        }
        if (!okHttp.getResponse().isSuccessful()){
            okHttp.close();
            throw new IllegalStateException("auth response code error code=>"+okHttp.getResponse().code());
        }
        String token = okHttp.getBody();
        if (JsonUtil.isJsonObject(token)){
            JSONObject jsonObject = JSON.parseObject(token);
            if (Fc.isNotBlank(authInfo.getCodeField())){
                String code = DeepJson.findString(jsonObject,authInfo.getCodeField());
                if (Fc.isNull(code) || !Fc.equals(code,authInfo.getSuccessCode())){
                    throw new IllegalStateException("auth response restful-code error code=>"+code);
                }
            }

            if (Fc.isNotBlank(authInfo.getTokenPrefixField())){
                String prefix = DeepJson.findString(jsonObject,authInfo.getTokenPrefixField());
                authInfo.setTokenPrefix(prefix);
            }

            if (Fc.isNotBlank(authInfo.getTokenSuffixField())){
                String suffix = DeepJson.findString(jsonObject,authInfo.getTokenSuffixField());
                authInfo.setTokenSuffix(suffix);
            }

            if (Fc.isNotBlank(authInfo.getExpiresField())){
                Long expiresIn = DeepJson.findLong(jsonObject,authInfo.getExpiresField());
                if (!Fc.isNull(expiresIn)){
                    authInfo.setExpiresIn(expiresIn);
                }
            }

            if (Fc.isBlank(authInfo.getTokenField())){
                throw new NullArgumentException("auth request yml param error: jpower.monitor-restful.token-field配置为空");
            }
            token = DeepJson.findString(jsonObject,authInfo.getTokenField());
            if (Fc.isBlank(token)){
                throw new NullPointerException("auth request token error: 接口返回空得token");
            }
        }
        return token;
    }

}
