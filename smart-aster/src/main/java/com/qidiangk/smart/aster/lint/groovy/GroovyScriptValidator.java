package com.qidiangk.smart.aster.lint.groovy;

import com.qidiangk.smart.aster.pojo.vo.ScriptIssueVO;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.control.messages.WarningMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Groovy脚本完整校验器
 * 支持错误信息、警告信息、代码质量检测
 */
public class GroovyScriptValidator {
    public static List<ScriptIssueVO> validateScript(String script, Map<String, Object> params) {
        List<ScriptIssueVO> issues = new ArrayList<>();

        CompilerConfiguration config = new CompilerConfiguration();
        config.setDebug( true);
//        config.setTolerance(10);
        // 设置警告级别为最高
        config.setWarningLevel(WarningMessage.PARANOIA);
        // 启用所有可能的警告
        config.getOptimizationOptions().put("groovy.warnings", true);
        config.setTargetDirectory(new File("/tmp")); // 设置临时目标目录

        Binding binding = new Binding();
        params.forEach(binding::setVariable);

        // 使用自定义的错误收集器
        GroovyShell shell = new GroovyShell(binding, config);

        try {
//             尝试解析脚本（不执行）
            shell.parse(script, "ValidatingScript.groovy");
        } catch (MultipleCompilationErrorsException e) {
            // 提取具体的编译错误信息
//            System.out.println(e.getErrorCollector().getWarningCount());
//            System.out.println(e.getErrorCollector().getErrorCount());
            for (Object message : e.getErrorCollector().getErrors()) {
                if (message instanceof SyntaxErrorMessage sem) {
                    issues.add(convertToScriptIssue(sem, "error"));
                } else {
                    System.out.println("其他错误.................");
                }
            }
        } catch (Exception e) {
            issues.add(new ScriptIssueVO(0, 0, 0, 0,
                    "解析时发生异常: " + e.getMessage(), "error"));
        }

        return issues;
    }

    private static ScriptIssueVO convertToScriptIssue(SyntaxErrorMessage sem, String type) {
        // 从 SyntaxErrorMessage 提取行列信息
        int line = sem.getCause().getLine();
        int column = Math.max(sem.getCause().getStartColumn(), 1);
        int endLine = sem.getCause().getEndLine();
        int endColumn = sem.getCause().getEndColumn();

        // 注意：这里的行列需要在前端转换为字符索引 (from/to)
        return new ScriptIssueVO(line, column, endLine, endColumn,
                sem.getCause().getMessage(), type);
    }

}