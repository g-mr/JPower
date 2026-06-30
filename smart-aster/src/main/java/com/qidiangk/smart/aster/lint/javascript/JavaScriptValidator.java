package com.qidiangk.smart.aster.lint.javascript;

import cn.hutool.core.collection.ListUtil;
import com.qidiangk.smart.aster.pojo.vo.ScriptIssueVO;

import javax.script.*;
import java.util.List;
import java.util.Map;

/**
 * javascript脚本完整校验器
 * 支持错误信息、警告信息、代码质量检测
 */
public class JavaScriptValidator {
    // 方法1：简单语法校验
    public static List<ScriptIssueVO> validateScript(String jsCode, Map<String, Object> params) {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("nashorn");

        Bindings bindings = engine.createBindings();
        bindings.putAll(params);
        // 限制危险对象访问（沙箱化）
        bindings.put("console", null);   // 禁用console
        bindings.put("Java", null);      // 禁止调用Java类
        bindings.put("exit", null);      // 禁用退出
        bindings.put("quit", null);      // 禁用退出

        try {
            // 编译脚本，不执行
            Compilable compilable = (Compilable) engine;
            compilable.compile("'use strict';\n"+jsCode);
            return ListUtil.empty();
        } catch (ScriptException e) {
            return ListUtil.of(convertToScriptIssue(e));
        }
    }

    private static ScriptIssueVO convertToScriptIssue(ScriptException e) {
        // 从 SyntaxErrorMessage 提取行列信息
        Integer line = e.getLineNumber() - 1;
        Integer column = e.getColumnNumber();
        Integer endLine = null;
        Integer endColumn = null;

        // 注意：这里的行列需要在前端转换为字符索引 (from/to)
        return new ScriptIssueVO(line, column, endLine, endColumn,
                e.getMessage(), "error");
    }

}