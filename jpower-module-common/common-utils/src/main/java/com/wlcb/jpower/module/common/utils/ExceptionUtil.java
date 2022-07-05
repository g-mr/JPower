package com.wlcb.jpower.module.common.utils;


/**
 * 异常工具
 *
 * @author mr.g
 **/
public class ExceptionUtil extends cn.hutool.core.exceptions.ExceptionUtil {

    /**
     * 堆栈转为完整字符串.<br/>
     * 不限制输出长度
     *
     * @param ex 异常
     * @return 返回的堆栈信息
     */
    public static String getStackTraceAsString(Throwable ex) {
        return stacktraceToString(ex,-1);
    }
}
