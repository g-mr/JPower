package com.wlcb.jpower.feign.interceptor;

import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import feign.Logger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.springframework.core.Ordered;

import java.io.EOFException;
import java.io.IOException;
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
        } catch (Exception e) {
            log.error("【feign】{} {} request fail:{}",request.method(),request.url(), NEWLINE + ExceptionsUtil.getStackTraceAsString(e));
            throw e;
        }finally {
            if (level != Logger.Level.NONE && Fc.notNull(response)){
                try {
                    printLog(request,response,connection,time);
                }catch (Exception e){
                    log.error("【feign】 Error printing request log:{}", NEWLINE+ExceptionsUtil.getStackTraceAsString(e));
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
            ResponseBody responseBody = response.body();
            if (Fc.notNull(responseBody)){
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
        try (BufferedSource source = responseBody.source()){
            //缺这行会拿到一个空的Buffer
            source.request(Long.MAX_VALUE);
            Buffer buffer = source.getBuffer();

            if (isReadable(buffer)){

                Charset charset = CharsetKit.CHARSET_UTF_8;
                if (responseBody.contentType() != null){
                    charset = responseBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                }

                return buffer.readString(charset);
            }
            return "omit bodyContent";
        } catch (IOException e) {
            return "(unknown bodyContent)";
        }
    }

    private String readRequestBody(RequestBody requestBody) {
        try(Buffer buffer = new Buffer()){
            requestBody.writeTo(buffer);
            if (isReadable(buffer)){

                Charset charset = CharsetKit.CHARSET_UTF_8;
                if (requestBody.contentType() != null){
                    charset = requestBody.contentType().charset(CharsetKit.CHARSET_UTF_8);
                }

                return buffer.readString(charset);
            }
            return "omit bodyContent";
        }catch (IOException e){
            log.error("读取requestBody出错:{}",NEWLINE+ExceptionsUtil.getStackTraceAsString(e));
            return "(unknown bodyContent)";
        }
    }

    private boolean isReadable(Buffer buffer) {
        try{
            for (int i = 0; i < 64 && buffer.exhausted() && buffer.size()>64; i++) {
                int codePoint = buffer.readUtf8CodePoint();
                if (Character.isIdentifierIgnorable(codePoint) && Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}
