package top.jpower.core.asterisk.utils.support;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.lang.Nullable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class FutureEvaluationContext extends StandardEvaluationContext {

    public FutureEvaluationContext() {
        super();
    }

    @Override
    @Nullable
    public Object lookupVariable(String name) {
        Object value = super.lookupVariable(name);
        if (value instanceof Future<?> future) {
            try {
                value = future.get(1, TimeUnit.MINUTES);
                super.setVariable(name, value);
            } catch (InterruptedException | ExecutionException e) {
                log.warn("获取值[{}]失败===>>{}", name, ExceptionUtil.stacktraceToString(e));
                super.setVariable(name, null);
            } catch (TimeoutException e) {
                log.warn("获取值[{}]超时===>>{}", name, e.getMessage());
            }
        }
        return value;
    }

}
