package com.wlcb.jpower.module.common.utils;

import com.google.common.base.Joiner;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import org.apache.http.HttpException;

import java.util.Map;

/**
 * OkHttp工具类
 *
 * @author mr.g
 **/
@Slf4j
public class OkHttp {

    /**
     * JSON数据结构
     **/
    public static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    /**
     * XML数据结构
     **/
    public static MediaType XML = MediaType.parse("application/xml; charset=utf-8");

    /**
     * Request
     **/
    @Getter
    private Request request;
    /**
     * Response
     **/
    @Getter
    private Response response = null;
    /**
     * 错误信息
     **/
    @Getter
    private String error = null;
    /**
     * 执行时间
     **/
    @Getter
    private Long responseTime;

    public OkHttp(Request request){
        this.request = request;
    }

    /**
     * GET请求
     *
     * @param url     请求的url
     * @return OkHttp
     */
    public static OkHttp get(String url) {
        return get(url, null, null);
    }

    /**
     * GET请求
     *
     * @param url     请求的url
     * @param queries 请求的参数
     * @return OkHttp
     */
    public static OkHttp get(String url, Map<String, String> queries) {
        return get(url, null, queries);
    }

    /**
     * GET请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param queries 请求的参数
     * @return OkHttp
     */
    public static OkHttp get(String url, Map<String, String> header, Map<String, String> queries) {
        StringBuilder sb = new StringBuilder(url);
        if (Fc.notNull(queries) && queries.size() > 0){
            sb.append(Joiner.on(StringPool.AMPERSAND).withKeyValueSeparator(StringPool.EQUALS).join(queries));
        }
        Request request = requestBuilder(header).url(sb.toString()).get().build();
        return new OkHttp(request);
    }

    /**
     * 构建Request
     *
     * @author mr.g
     * @param header 请求头
     * @return okhttp3.Request.Builder
     **/
    private static Request.Builder requestBuilder(Map<String, String> header){
        Request.Builder builder = new Request.Builder();
        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        return builder;
    }

    /**
     * HEAD请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param queries 请求的参数
     * @return OkHttp
     */
    public static OkHttp head(String url, Map<String, String> header, Map<String, String> queries) {
        StringBuffer sb = new StringBuffer(url);
        if (Fc.notNull(queries) && queries.size() > 0){
            sb.append(Joiner.on(StringPool.AMPERSAND).withKeyValueSeparator(StringPool.EQUALS).join(queries));
        }
        Request request = requestBuilder(header).url(sb.toString()).head().build();
        return new OkHttp(request);
    }

    /**
     * DELETE请求
     *
     * @param url     请求的url
     * @param queries 请求的参数，在浏览器？后面的数据，没有可以传null
     * @return OkHttp
     */
    public static OkHttp delete(String url, Map<String, String> queries) {
        return delete(url, null, queries);
    }

    /**
     * DELETE请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param params 请求的参数
     * @return OkHttp
     */
    public static OkHttp delete(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        //添加参数
        if (Fc.notNull(params) && params.size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request request = requestBuilder(header).url(url).delete(formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * PUT请求
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return OkHttp
     */
    public static OkHttp put(String url, Map<String, String> params) {
        return put(url, null, params);
    }

    /**
     * PUT请求
     *
     * @param url     请求的url
     * @param header  请求头
     * @param params form 提交的参数
     * @return OkHttp
     */
    public static OkHttp put(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        //添加参数
        if (Fc.notNull(params) && params.size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request request = requestBuilder(header).url(url).put(formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * POST请求
     *
     * @param url    请求的url
     * @param params post form 提交的参数
     * @return OkHttp
     */
    public static OkHttp post(String url, Map<String, String> params) {
        return post(url, null, params);
    }

    /**
     * POST请求
     *
     * @param url    请求的url
     * @param header 请求头
     * @param params post form 提交的参数
     * @return OkHttp
     */
    public static OkHttp post(String url, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        //添加参数
        if (Fc.notNull(params) && params.size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request request = requestBuilder(header).url(url).post(formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * 请求
     *
     * @author mr.g
     * @param url   请求的url
     * @param method    请求方式
     * @param header    请求头
     * @param params    请求参数
     * @return OkHttp
     **/
    public static OkHttp method(String url, String method, Map<String, String> header, Map<String, String> params) {
        FormBody.Builder formBuilder = new FormBody.Builder();
        //添加参数
        if (Fc.notNull(params) && params.size() > 0) {
            params.forEach(formBuilder::add);
        }

        Request request = requestBuilder(header).url(url).method(method.toUpperCase(),formBuilder.build()).build();
        return new OkHttp(request);
    }

    /**
     * POST请求发送JSON数据
     *
     * @param url     请求的url
     * @param json    请求的json串
     * @return OkHttp
     */
    public static OkHttp postJson(String url, String json) {
        return postJson(url, null, json);
    }

    /**
     * POST请求发送JSON数据
     * @param url     请求的url
     * @param header  请求头
     * @param json    请求的json串
     * @return OkHttp
     */
    public static OkHttp postJson(String url, Map<String, String> header, String json) {
        return postContent(url, header, json, JSON);
    }

    /**
     * POST请求发送xml数据
     *
     * @param url     请求的url
     * @param xml     请求的xml串
     * @return OkHttp
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
     * @return OkHttp
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
     * @return OkHttp
     */
    public static OkHttp postContent(String url, Map<String, String> header, String content, MediaType mediaType) {
        return content(url,"POST",header,content,mediaType);
    }

    /**
     * body参数请求
     *
     * @author mr.g
     * @param url   请求的url
     * @param method    请求方式
     * @param header    请求头
     * @param content   请求参数内容
     * @param mediaType 参数类型
     * @return OkHttp
     **/
    public static OkHttp content(String url,String method, Map<String, String> header, String content, MediaType mediaType) {
        RequestBody requestBody = RequestBody.create(mediaType, content);
        Request.Builder builder = new Request.Builder();

        if (header != null && header.keySet().size() > 0) {
            header.forEach(builder::addHeader);
        }
        Request request = builder.url(url).method(method.toUpperCase(),requestBody).build();
        return new OkHttp(request);
    }

    /**
     * PUT请求发送JSON数据
     *
     * @param url     请求的url
     * @param json    请求的json串
     * @return OkHttp
     */
    public static OkHttp putJson(String url, String json) {
        return putJson(url, null, json);
    }

    /**
     * PUT请求发送JSON数据
     * @param url     请求的url
     * @param header  请求头
     * @param json    请求的json串
     * @return OkHttp
     */
    public static OkHttp putJson(String url, Map<String, String> header, String json) {
        return putContent(url, header, json, JSON);
    }

    /**
     * PUT请求发送xml数据
     *
     * @param url     请求的url
     * @param xml     请求的xml串
     * @return OkHttp
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
     * @return OkHttp
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
     * @return OkHttp
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
     * 执行请求
     *
     * @author mr.g
     * @param interceptors 拦截器
     * @return OkHttp
     **/
    public OkHttp execute(Interceptor... interceptors) {
        return execute(true,true, interceptors);
    }

    /**
     * 执行请求
     *
     * @author mr.g
     * @param retryOnConnectionFailure 是否开启重试
     * @param followRedirects 是否开启重定向
     * @param interceptors 拦截器
     * @return OkHttp
     **/
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

            long startTime = System.currentTimeMillis();
            response = okHttpClient.newCall(request).execute();
            // 获取最终得request
            request = response.request();
            responseTime = (System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            error = e.getMessage();
            log.error("OkHttp3 execute error >> ex = {}", ExceptionUtil.getStackTraceAsString(e));
            throw new HttpException("OkHttp3 execute error >> ex = " + e.getMessage());
        }

        return this;
    }

    /**
     * 获取response得body,获取body后会直接把okhttp流关掉
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
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("OkHttp3 getBody error >> ex = {}", e.getMessage());
        }finally {
            close();
        }
        return responseBody;
    }

    /**
     * 获取request得body
     *
     * @author mr.g
     * @return java.lang.String
     **/
    public String getRequestBody() {
        try (Buffer buffer = new Buffer()){
            RequestBody requestBody = request.body();
            if (Fc.notNull(requestBody)){
                requestBody.writeTo(buffer);
                return buffer.readUtf8();
            }
        } catch (Exception e) {
            log.error("OkHttp3 read body error ==> {}",e.getMessage());
        }
        return null;
    }

    /**
     * 关闭
     */
    public void close() {
        if (Fc.notNull(response)) {
            response.close();
        }
    }

}
