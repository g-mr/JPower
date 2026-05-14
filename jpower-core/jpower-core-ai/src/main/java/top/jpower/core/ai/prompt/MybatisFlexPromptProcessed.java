package top.jpower.core.ai.prompt;

import com.mybatisflex.core.row.DbChain;
import com.mybatisflex.core.row.Row;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.ai.properties.AiProperties;
import top.jpower.core.util.utils.Fc;

/**
 * 数据库提示词处理
 *
 * @author mr.g
 */
@Slf4j
@RequiredArgsConstructor
public class MybatisFlexPromptProcessed implements PromptProcessed {

    private final AiProperties aiProperties;

    @Override
    public String process(String contextKey) {
        if (Fc.isBlank(contextKey)) {
            log.warn("提示词KEY[contextKey]是空的");
            return "";
        }

        Row row = DbChain.table(aiProperties.getPrompt().getDatabaseTable())
                .select("content")
                .where("context_key = ?",contextKey)
                .and("delete_time = 0")
                .one();
        if (Fc.isNull(row) || Fc.isBlank(row.getString("content"))) {
            log.warn("提示词KEY[{}]内容为空", contextKey);
            return "";
        }
        return row.getString("content");
    }

}
