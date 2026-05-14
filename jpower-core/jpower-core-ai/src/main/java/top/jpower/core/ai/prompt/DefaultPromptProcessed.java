package top.jpower.core.ai.prompt;

import lombok.extern.slf4j.Slf4j;

/**
 * 默认提示词处理
 *
 * @author mr.g
 */
@Slf4j
public class DefaultPromptProcessed implements PromptProcessed {

    @Override
    public String process(String contextKey) {
        return contextKey;
    }

}
