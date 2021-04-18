package com.wlcb.jpower.module.common.utils;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @Author mr.g
 * @Date 2021/4/18 0018 20:58
 */
public class JsUtil {

    private static final String KEY = "java";
    private static final String JAVASCRIPT = "JavaScript";

    public static <T> T execJsFunction(String js, String functionName , Object... objects) throws ScriptException, NoSuchMethodException {
        ScriptEngineManager m = new ScriptEngineManager();
        ScriptEngine engine = m.getEngineByName(JAVASCRIPT);
        if (js.contains(KEY)){
            throw new IllegalArgumentException("检查到非法关键字");
        }
        engine.eval(js);
        Invocable inv = (Invocable) engine;
        return (T) inv.invokeFunction(functionName,objects);
    }

}
