package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import io.seata.core.context.RootContext;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 接口请求完成后回滚事务，不要把测试数据保存到数据库中
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@Slf4j
public final class RollbackInterceptor implements Interceptor {

    @Override
    @SneakyThrows
    public Response intercept(Chain chain) {
        GlobalTransaction tx = null;
        Request request = chain.request();
        try{
            tx = GlobalTransactionContext.getCurrentOrCreate();
            tx.begin();
            request = chain.request().newBuilder().addHeader(RootContext.KEY_XID,tx.getXid()).build();
        }catch (Exception e){
            log.error("GlobalTransaction create fail; error==>{}",e.getMessage());
        }

        Response response = chain.proceed(request);

        if (!Fc.isNull(tx)){
            try {
                tx.rollback();
            }catch (Exception e){
                log.error("GlobalTransaction rollback fail; error==>{}",e.getMessage());
            }
        }
        return response;
    }
}
