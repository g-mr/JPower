package com.wlcb.jpower.feign.interceptor;

import com.wlcb.jpower.module.common.utils.ExceptionsUtil;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.constants.StringPool;
import feign.Logger;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Connection;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * feign日志打印
 * @Author mr.g
 * @date 2021-05-21 14:33
 **/
@Slf4j
@RequiredArgsConstructor
public class HttpLogInterceptor implements Interceptor {

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
            log.error("【feign】{} {} request error:{}{}",request.method(),request.url(), StringPool.NEWLINE, ExceptionsUtil.getStackTraceAsString(e));
            throw e;
        }finally {
            if (level != Logger.Level.NONE && Fc.notNull(response)){
                try {
                    printLog(request,response,connection,time);
                }catch (Exception e){
                    log.error("【feign】 Error printing request log:{}{}",StringPool.NEWLINE,ExceptionsUtil.getStackTraceAsString(e));
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
    private void printLog(Request request, Response response, Connection connection, long time) {

    }

}
