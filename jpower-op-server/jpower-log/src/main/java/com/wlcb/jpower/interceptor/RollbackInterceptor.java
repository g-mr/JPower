package com.wlcb.jpower.interceptor;

import com.wlcb.jpower.module.common.utils.Fc;
import io.seata.core.context.RootContext;
import io.seata.core.exception.TransactionException;
import io.seata.tm.api.GlobalTransaction;
import io.seata.tm.api.GlobalTransactionContext;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * 接口请求完成后回滚事务，不要把测试数据保存到数据库中
 * @Author mr.g
 * @Date 2021/4/4 0004 0:36
 */
@Slf4j
public final class RollbackInterceptor implements Interceptor {

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        GlobalTransaction tx = null;
        try{
            tx = GlobalTransactionContext.getCurrentOrCreate();
            //事务时间持续10.1秒
            tx.begin(10_100);
            request = chain.request().newBuilder().addHeader(RootContext.KEY_XID,tx.getXid()).build();
        }catch (Exception e){
            log.error("GlobalTransaction create fail; error==>{}",e.getMessage());
        }

        Response response;
        try {
            response = chain.proceed(request);
        } catch (Exception e) {
            throw e;
        }finally {
            if (!Fc.isNull(tx)){
                //针对监控得服务进行回滚
                try {
                    tx.rollback();
                }catch (Exception e){
                    log.error("GlobalTransaction rollback fail; error==>{}",e.getMessage());
                }finally {
                    try {
                        tx.suspend(true);
                    } catch (TransactionException e) {
                        log.error("GlobalTransaction suspend fail; error==>{}",e.getMessage());
                    }
                }
            }
        }
        return response;
    }
}
