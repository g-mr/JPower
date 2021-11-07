package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.BufferUtil;
import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.CharsetKit;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static com.wlcb.jpower.module.common.utils.constants.StringPool.NEWLINE;

/**
 * @author mr.g
 * @date 2021-05-25 11:49
 */
@Slf4j
public class LogInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        StringBuilder builder = new StringBuilder(NEWLINE);
        List<Object> args = new ArrayList<>();
        builder.append("==>TEST REST URL: {} {}").append(NEWLINE);
        args.add(request.method());
        args.add(request.url());

        if (log.isDebugEnabled()){
            RequestBody requestBody = request.body();
            if (Fc.isNull(requestBody)){
                builder.append("  TEST REST PARAMS BODYS: {}").append(NEWLINE);
                args.add(readRequestBody(requestBody));
            }


            builder.append("  TEST REST PARAMS HEADERS: ").append(NEWLINE);
            Headers hd = request.headers();
            for (int i = 0, count = hd.size(); i < count; i++) {
                builder.append("    {} = {}").append(NEWLINE);
                args.add(hd.name(i));
                args.add(hd.value(i));
            }
        }

        Response response = null;
        Exception exception = null;
        long startTime = System.currentTimeMillis();
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            exception = e;
            throw e;
        }finally {
            if (Fc.isNull(response)){
                builder.append("<==END TEST FAILED ({}ms) : {}");
                args.add(System.currentTimeMillis() - startTime);
                args.add(ExceptionsUtil.getStackTraceAsString(exception));
            }else {
                if (HttpHeaders.hasBody(response)){
                    ResponseBody responseBody = response.body();
                    long contentLength = responseBody.contentLength();
                    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";

                    builder.append("  RESULT: {}-byte body  {}").append(NEWLINE).append("    {}");
                    args.add(bodySize);
                    args.add(responseBody.contentLength());
                    args.add(readResponseBody(responseBody));
                }


                builder.append("<==END REQUEST TEST REST {} {} {}");
                args.add(response.code());
                args.add((response.message().isEmpty() ? "" : ' ' + response.message()));
                args.add(" (" + (System.currentTimeMillis() - startTime) + "ms)");
            }
            log.info(builder.toString(),args.toArray());
        }

        return response;
    }

    private String readResponseBody(ResponseBody responseBody) {
        if (Fc.notNull(responseBody)){
            try {
                BufferedSource source = responseBody.source();
                //缺这行会拿到一个空的Buffer
                source.request(Long.MAX_VALUE);
                Buffer buffer = source.getBuffer();

                if (BufferUtil.isReadable(buffer)){

                    Charset charset = StandardCharsets.UTF_8;
                    if (Fc.notNull(responseBody.contentType())){
                        charset = responseBody.contentType().charset(charset);
                    }

                    return buffer.clone().readString(charset);
                }
                return "bodyContent 省略";
            } catch (IOException e) {
                return "读取 bodyContent 出错";
            }
        }else {
            return "responseBody is null";
        }
    }

    private String readRequestBody(RequestBody requestBody) {
        try(Buffer buffer = new Buffer()){
            if (Fc.notNull(requestBody)){
                requestBody.writeTo(buffer);
                if (BufferUtil.isReadable(buffer)){
                    Charset charset = CharsetKit.CHARSET_UTF_8;
                    if (requestBody.contentType() != null){
                        charset = requestBody.contentType().charset(charset);
                    }

                    return buffer.readString(charset);
                }
                return "省略 bodyContent";
            }
            return "requestBody is null";
        }catch (IOException e){
            return "读取 bodyContent 出错";
        }
    }

}
