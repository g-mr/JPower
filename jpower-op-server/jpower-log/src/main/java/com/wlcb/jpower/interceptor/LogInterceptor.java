package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.OkhttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.internal.http.HttpHeaders;

import java.io.IOException;
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
                args.add(OkhttpUtil.readRequestBody(requestBody));
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
                    args.add(OkhttpUtil.readResponseBody(responseBody));
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

}
