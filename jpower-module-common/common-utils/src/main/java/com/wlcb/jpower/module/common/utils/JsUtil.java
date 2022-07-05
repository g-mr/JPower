package com.wlcb.jpower.module.common.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * JS工具
 *
 * @author mr.g
 */
public class JsUtil {

    /**
     * 执行JS脚本中的方法
     *
     * @author mr.g
     * @param js 脚本
     * @param func 方法名
     * @param args 参数
     * @return 执行结果
     **/
    public static <T> T execJsFunction(String js, String func , Object... args) throws ScriptException, NoSuchMethodException, IllegalArgumentException {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine engine = m.getEngineByName("JavaScript");
        if (js.contains("java")){
            throw new IllegalArgumentException("检测到非法关键字");
        }
        engine.eval(js);
        Invocable inv = (Invocable) engine;
        return (T) inv.invokeFunction(func,args);
    }

}
