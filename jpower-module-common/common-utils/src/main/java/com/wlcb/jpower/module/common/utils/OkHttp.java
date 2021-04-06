package com.wlcb.jpower.module.common.utils;

import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpException;

import java.util.Map;

/**
 * @Author mr.g
 * @Date 2021/4/2 0002 23:18
 */
@Slf4j
public class OkHttp {
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    @Getter
    private final Request request;
    @Getter
    private Response response;
    @Getter
    private String error;

    public OkHttp(Request request){
        this.request = request;
    }

    /**
     * GET
     *
     * @param url     请求的url
     * @return String
     */
    public static OkHttp get(String url) {
        return get(url, null, null);
    }

    /**
     * GET
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return String
     */
    public static OkHttp get(String url, Map<String, String> queries) {
        return get(url, null, queries);
    }

    /**
     * GET
     *
     * @param url     请求的url
     * @param header  请求头
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return String
     */
    public static OkHttp get(String url, Map<String, String> header, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?clientId=jpower");
        if (queries != null && queries.keySet().size() > 0) {
            queries.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(sb.toString()).get().build();
        return new OkHttp(request);
    }

    /**
     * HEAD
     *
     * @param url     请求的url
     * @param header  请求头
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return String
     */
    public static OkHttp head(String url, Map<String, String> header, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        sb.append("?clientId=jpower");
        if (queries != null && queries.keySet().size() > 0) {
            queries.forEach((k, v) -> sb.append("&").append(k).append("=").append(v));
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(sb.toString()).head().build();
        return new OkHttp(request);
    }

    /**
     * DELETE
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return String
     */
    public static OkHttp delete(String url, Map<String, String> queries) {
        return delete(url, null, queries);
    }

    /**
     * DELETE
     *
     * @param url     请求的url
     * @param header  请求头
     * @param params 请求的参数
     * @return String
     */
    public static OkHttp delete(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder().add("clientId", "jpower");
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(url).delete(formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * PUT
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return String
     */
    public static OkHttp put(String url, Map<String, String> params) {
        return put(url, null, params);
    }

    /**
     * PUT
     *
     * @param url     请求的url
     * @param header  请求头
     * @param params form 提交的参数
     * @return String
     */
    public static OkHttp put(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder().add("clientId", "jpower");
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }

        Request request = builder.url(url).put(formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * POST
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return String
     */
    public static OkHttp post(String url, Map<String, String> params) {
        return post(url, null, params);
    }

    /**
     * POST
     *
     * @param url    请求的url
     * @param header 请求头
     * @param params post form 提交的参数
     * @return String
     */
    public static OkHttp post(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder().add("clientId", "jpower");
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }

        Request request = builder.url(url).post(formBuilder.build()).build();
        return new OkHttp(request);
    }

    public static OkHttp method(String url, String method, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder().add("clientId", "jpower");
        //添加参数
        if (params != null && params.keySet().size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }

        Request request = builder.url(url).method(method,formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * POST请求发送JSON数据
     *
     * @param url     请求的url
     * @param json    请求的json串
     * @return String
     */
    public static OkHttp postJson(String url, String json) {
        return postJson(url, null, json);
    }

    /**
     * POST请求发送JSON数据
     * @param url     请求的url
     * @param header  请求头
     * @param json    请求的json串
     * @return String
     */
    public static OkHttp postJson(String url, Map<String, String> header, String json) {
        return postContent(url, header, json, JSON);
    }

    /**
     * POST请求发送xml数据
     *
     * @param url     请求的url
     * @param xml     请求的xml串
     * @return String
     */
    public static OkHttp postXml(String url, String xml) {
        return postXml(url, null, xml);
    }

    /**
     * POST请求发送xml数据
     *
     * @param url     请求的url
     * @param header  请求头
     * @param xml     请求的xml串
     * @return String
     */
    public static OkHttp postXml(String url, Map<String, String> header, String xml) {
        return postContent(url, header, xml, XML);
    }

    /**
     * 发送POST请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param content 请求内容
     * @param mediaType 请求类型
     * @return String
     */
    public static OkHttp postContent(String url, Map<String, String> header, String content, MediaType mediaType) {
        return content(url,"POST",header,content,mediaType);
    }

    public static OkHttp content(String url,String method, Map<String, String> header, String content, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(mediaType, content);
        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(url).method(method,requestBody).build();
        return new OkHttp(request);
    }

    /**
     * PUT请求发送JSON数据
     *
     * @param url     请求的url
     * @param json    请求的json串
     * @return String
     */
    public static OkHttp putJson(String url, String json) {
        return putJson(url, null, json);
    }

    /**
     * PUT请求发送JSON数据
     * @param url     请求的url
     * @param header  请求头
     * @param json    请求的json串
     * @return String
     */
    public static OkHttp putJson(String url, Map<String, String> header, String json) {
        return putContent(url, header, json, JSON);
    }

    /**
     * PUT请求发送xml数据
     *
     * @param url     请求的url
     * @param xml     请求的xml串
     * @return String
     */
    public static OkHttp putXml(String url, String xml) {
        return putXml(url, null, xml);
    }

    /**
     * PUT请求发送xml数据
     *
     * @param url     请求的url
     * @param header  请求头
     * @param xml     请求的xml串
     * @return String
     */
    public static OkHttp putXml(String url, Map<String, String> header, String xml) {
        return putContent(url, header, xml, XML);
    }

    /**
     * 发送PUT请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param content 请求内容
     * @param mediaType 请求类型
     * @return String
     */
    public static OkHttp putContent(String url, Map<String, String> header, String content, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(mediaType, content);
        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(url).put(requestBody).build();
        return new OkHttp(request);
    }

    /**
     * 执行
     *
     * @return String
     */
    public OkHttp execute(Interceptor... interceptors) {
        return execute(true,true, interceptors);
    }


    /**
     * 执行
     *
     * @return String
     */
    @SneakyThrows
    public OkHttp execute(Boolean retryOnConnectionFailure,Boolean followRedirects,Interceptor... interceptors) {
        if (Fc.isNull(request)){
            throw new HttpException("OkHttp3 request is null");
        }
        try {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.retryOnConnectionFailure(retryOnConnectionFailure);
            builder.followRedirects(followRedirects);
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
            OkHttpClient okHttpClient = builder.build();
            response = okHttpClient.newCall(request).execute();
        } catch (Exception e) {
            error = e.getMessage();
            log.error("OkHttp3 execute error >> ex = {}", e.getMessage());
        }

        return this;
    }

    /**
     * 获取body
     *
     * @return String
     */
    @SneakyThrows
    public String getBody() {
        if (Fc.isNull(response)){
            throw new HttpException("OkHttp3 response is null");
        }

        String responseBody = StringPool.EMPTY;
        try {
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("OkHttp3 getBody error >> ex = {}", e.getMessage());
        }finally {
            close();
        }
        return responseBody;
    }

    /**
     * 关闭
     *
     * @return String
     */
    public OkHttp close() {
        if (!Fc.isNull(response)) {
            response.close();
        }
        return this;
    }

}
