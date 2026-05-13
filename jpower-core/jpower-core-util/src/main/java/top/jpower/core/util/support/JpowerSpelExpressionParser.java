package top.jpower.core.util.support;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import org.springframework.expression.Expression;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.SpelCompilerMode;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;

public class JpowerSpelExpressionParser extends SpelExpressionParser {

    // 配置安全解析器（禁用危险操作）
    private final static SpelParserConfiguration config = new SpelParserConfiguration(
            SpelCompilerMode.OFF, // 禁用编译（减少风险）
            null                 // 自定义 ClassLoader（可限制类加载）
    );

    public JpowerSpelExpressionParser() {
        super(config);
    }

    public Expression parseExpression(String expressionString) throws ParseException {
        if (!(StrUtil.containsAny(expressionString,
                CharPool.SINGLE_QUOTE,
                '#',
                '+',
                CharPool.DASHED,
                '*',
                '/',
                '%') || NumberUtil.isNumber(expressionString))){
            expressionString = CharPool.SINGLE_QUOTE+expressionString+CharPool.SINGLE_QUOTE;
        }
        return super.parseExpression(expressionString);
    }

}
