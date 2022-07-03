package com.wlcb.jpower.module.common.utils;


/**
 * Utility methods for terminal atomics with Throwables.
 *
 * @since 1.1.2
 */
public class ExceptionUtil extends cn.hutool.core.exceptions.ExceptionUtil {

    /**
     * 堆栈转为完整字符串.
     * 不限制输出长度
     *
     * @param ex 异常
     * @return 返回的堆栈信息
     */
    public static String getStackTraceAsString(Throwable ex) {
        return stacktraceToString(ex,-1);
    }
}
