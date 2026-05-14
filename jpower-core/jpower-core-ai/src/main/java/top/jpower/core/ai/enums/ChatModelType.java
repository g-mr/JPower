package top.jpower.core.ai.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AI 模型类型枚举
 *
 * @author mr.g
 */
@Getter
@AllArgsConstructor
public enum ChatModelType {

    /**
     * DeepSeek 模型
     */
    DEEPSEEK("deepseek", "DeepSeek"),

    /**
     * 通义千问（DashScope）
     */
    DASHSCOPE("dashscope", "通义千问"),

    /**
     * OpenAI 模型
     */
    OPENAI("openai", "OpenAI"),

    /**
     * Azure OpenAI 模型
     */
    AZURE_OPENAI("azure-openai", "Azure OpenAI"),

    /**
     * Ollama 本地模型
     */
    OLLAMA("ollama", "Ollama"),

    /**
     * ZhiPu AI（智谱）
     */
    ZHIPU("zhipu", "智谱AI"),
    ;

    /**
     * 模型标识（对应 bean name 前缀）
     */
    private final String key;

    /**
     * 模型描述
     */
    private final String description;

    /**
     * 根据 key 获取模型类型
     */
    public static ChatModelType of(String key) {
        if (key == null) {
            return null;
        }
        for (ChatModelType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
