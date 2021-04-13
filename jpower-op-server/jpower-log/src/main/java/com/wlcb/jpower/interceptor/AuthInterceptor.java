package com.wlcb.jpower.interceptor;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.CharMatcher;
import com.google.common.base.Functions;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.primitives.Chars;
import com.wlcb.jpower.module.common.utils.*;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import com.wlcb.jpower.properties.AuthInfoConfiguration;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang.ArrayUtils;
import org.apache.http.HttpException;

import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@Slf4j
@AllArgsConstructor
public final class AuthInterceptor implements Interceptor {

    private static final String CACHE_NAME = "RESTFUL-MONITOR-AUTH";

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
    private String requestToken(String domain) {

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
            //如何深结构获取数据
        }
        return token;
    }

    public static void main(String[] args) {


        String keyss = "code[0].data";
        JSONObject jsonObject = JSON.parseObject("{\n" +
                "\t\"code\": [{\"data\":\"data1\"},{\"data\":\"data2\"}],\n" +
                "\t\"data\": {\n" +
                "\t\t\"accessToken\": \"asdmklasjmdloasjmdas\",\n" +
                "\t\t\"expiresIn\": 0,\n" +
                "\t\t\"refreshToken\": \"\",\n" +
                "\t\t\"tokenType\": \"\",\n" +
                "\t\t\"user\": {\n" +
                "\t\t\t\"address\": \"\",\n" +
                "\t\t\t\"avatar\": \"\",\n" +
                "\t\t\t\"birthday\": \"\",\n" +
                "\t\t\t\"clientCode\": \"\",\n" +
                "\t\t\t\"email\": \"\",\n" +
                "\t\t\t\"idNo\": \"\",\n" +
                "\t\t\t\"idType\": 0,\n" +
                "\t\t\t\"isSysUser\": 0,\n" +
                "\t\t\t\"lastLoginTime\": \"\",\n" +
                "\t\t\t\"loginCount\": 0,\n" +
                "\t\t\t\"loginId\": \"\",\n" +
                "\t\t\t\"nickName\": \"\",\n" +
                "\t\t\t\"orgId\": \"\",\n" +
                "\t\t\t\"orgName\": \"\",\n" +
                "\t\t\t\"otherCode\": \"\",\n" +
                "\t\t\t\"postCode\": \"\",\n" +
                "\t\t\t\"roleIds\": [],\n" +
                "\t\t\t\"telephone\": \"\",\n" +
                "\t\t\t\"tenantCode\": \"\",\n" +
                "\t\t\t\"userId\": \"\",\n" +
                "\t\t\t\"userName\": \"\",\n" +
                "\t\t\t\"userType\": 0\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"message\": \"请求成功\",\n" +
                "\t\"status\": true\n" +
                "}");
        System.out.println(JsonUtil.find(jsonObject,keyss));
//        String[] ss = Fc.toStrArray(".", keyss);
//        for (int i = 0; i < ss.length; i++) {
//
//        }




//        for (String s : ss) {
//            jsonObject.getJSONObject(s);
//        }
    }
}
