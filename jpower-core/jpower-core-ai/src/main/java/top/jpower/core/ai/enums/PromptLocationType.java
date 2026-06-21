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
public enum PromptLocationType {

    /**
     * MybatisFlex
     */
    MYBATIS_FLEX("MybatisFlex"),

    /**
     * Nacos
     */
    NACOS("Nacos"),
    ;

    /**
     * 模型标识（对应 bean name 前缀）
     */
    private final String key;

    /**
     * 根据 key 获取模型类型
     */
    public static PromptLocationType of(String key) {
        if (key == null) {
            return null;
        }
        for (PromptLocationType type : values()) {
            if (type.key.equalsIgnoreCase(key)) {
                return type;
            }
        }
        return null;
    }
}
