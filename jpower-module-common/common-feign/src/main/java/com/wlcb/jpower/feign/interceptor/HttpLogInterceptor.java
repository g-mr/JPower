package com.wlcb.jpower.feign.interceptor;

import com.wlcb.jpower.module.common.utils.BufferUtil;
import com.wlcb.jpower.module.common.utils.ExceptionUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import feign.Logger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;
import org.springframework.core.Ordered;

import java.io.IOException;
import java.net.SocketException;
import java.nio.charset.Charset;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.*;

/**
 * feign日志打印
 * @Author mr.g
 * @date 2021-05-21 14:33
 **/
@Slf4j
@RequiredArgsConstructor
public class HttpLogInterceptor implements Interceptor, Ordered {

    private final Logger.Level level;

    @Override
    @SneakyThrows
    public Response intercept(Chain chain) {
        Request request = chain.request();
        Connection connection = chain.connection();
        Response response = null;
        long time = 0L;
        try {
            long startTime = System.currentTimeMillis();
            response = chain.proceed(request);
            time = System.currentTimeMillis() - startTime;
            return response;
        } catch (SocketException e){
            // feign socket连接经常会出现不稳定情况,okhttp的重试机制会再次请求并通过
            log.warn("【feign】{} {} request fail:{}",request.method(),request.url(), e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("【feign】{} {} request fail:{}",request.method(),request.url(), NEWLINE + ExceptionUtil.getStackTraceAsString(e));
            throw e;
        } finally {
            if (level != Logger.Level.NONE && Fc.notNull(response)){
                try {
                    printLog(request,response,connection,time);
                }catch (Exception e){
                    log.error("【feign】 Error printing request log:{}", NEWLINE+ExceptionUtil.getStackTraceAsString(e));
                }
            }
        }
    }

    /**
     * 打印日志
     * @Author mr.g
     * @param request
     * @param response
     * @param connection
     * @param time
     * @return void
     **/
    private void printLog(Request request, Response response, Connection connection, long time) throws IOException {
        StringBuilder builder = new StringBuilder(NEWLINE+"============start feign http=============").append(NEWLINE);
        builder.append("-->")
                .append(request.method()).append(SPACE)
                .append(request.url()).append(SPACE)
                .append(Fc.notNull(connection)?connection.protocol():EMPTY)
                .append(NEWLINE);

        if (level == Logger.Level.HEADERS || level == Logger.Level.FULL){
            builder.append("request headers: ").append(NEWLINE);
            Headers requestHeaders = request.headers();
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                builder.append(TAB).append(requestHeaders.name(i)).append(SPACE).append(EQUALS).append(SPACE).append(requestHeaders.value(i)).append(NEWLINE);
            }
        }

        String rqBody = "";
        if (level == Logger.Level.FULL){
            RequestBody requestBody = request.body();
            if (Fc.notNull(requestBody)){
                builder.append("request body: ")
                        .append(requestBody.contentLength() != -1 ? requestBody.contentLength() + "-byte" : "unknown-length")
                        .append(SPACE).append(requestBody.contentType())
                        .append(NEWLINE);

                rqBody = readRequestBody(requestBody);
                builder.append(TAB)
                        .append(rqBody)
                        .append(NEWLINE);
            }else {
                builder.append("request body is null").append(NEWLINE);
            }
        }

        builder.append("--> end request").append(LEFT_BRACKET).append(rqBody.getBytes().length).append("-byte body").append(RIGHT_BRACKET).append(NEWLINE);

        builder.append(NEWLINE);

        builder.append("<--")
                .append(response.protocol()).append(SPACE)
                .append(response.code()).append(SPACE)
                .append(response.message()).append(SPACE)
                .append(LEFT_BRACKET).append(time).append("ms").append(RIGHT_BRACKET).append(SPACE)
                .append(NEWLINE);

        if (level == Logger.Level.HEADERS || level == Logger.Level.FULL){
            builder.append("response headers: ").append(NEWLINE);
            Headers responseHeaders = response.headers();
            for (int i = 0, count = responseHeaders.size(); i < count; i++) {
                builder.append(TAB).append(responseHeaders.name(i)).append(SPACE).append(EQUALS).append(SPACE).append(responseHeaders.value(i)).append(NEWLINE);
            }
        }

        String rpBody = "";
        if (level == Logger.Level.FULL){
            if (HttpHeaders.hasBody(response)){
                ResponseBody responseBody = response.body();

                builder.append("response body: ")
                        .append(responseBody.contentLength() != -1 ? responseBody.contentLength() + "-byte" : "unknown-length")
                        .append(SPACE).append(responseBody.contentType())
                        .append(NEWLINE);

                rpBody = readResponseBody(responseBody);

                builder.append(TAB)
                        .append(rpBody)
                        .append(NEWLINE);
            }else {
                builder.append("response body is null").append(NEWLINE);
            }
        }

        builder.append("<-- end response").append(LEFT_BRACKET).append(rpBody.getBytes().length).append("-byte body").append(RIGHT_BRACKET).append(NEWLINE);
        builder.append("============end feign http=============");
        log.info(builder.toString());
    }

    private String readResponseBody(ResponseBody responseBody) {
        if (Fc.notNull(responseBody)){
            try {
                BufferedSource source = responseBody.source();
                //缺这行会拿到一个空的Buffer
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();

                Charset charset = CharsetKit.CHARSET_UTF_8;
                if (responseBody.contentType() != null){
                    charset = responseBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                }
                return BufferUtil.cloneRead(buffer,charset);
            } catch (IOException e) {
                return "(unknown bodyContent)";
            }
        }else {
            return "responseBody is null";
        }
    }

    private String readRequestBody(RequestBody requestBody) {
        try(Buffer buffer = new Buffer()){
            if (Fc.notNull(requestBody)){
                requestBody.writeTo(buffer);

                Charset charset = CharsetKit.CHARSET_UTF_8;
                if (requestBody.contentType() != null){
                    charset = requestBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                }
                return BufferUtil.read(buffer,charset);
            }
            return "requestBody is null";
        }catch (IOException e){
            return "(unknown bodyContent)";
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
