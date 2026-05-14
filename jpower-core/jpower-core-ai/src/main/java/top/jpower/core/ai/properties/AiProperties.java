package top.jpower.core.ai.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.jpower.core.ai.enums.ChatModelType;
import top.jpower.core.ai.enums.PromptLocationType;

/**
 * AI 配置属性
 * <p>
 * 配置示例:
 * <pre>
 * jpower:
 *   ai:
 *     primary: deepseek  # 默认使用的模型类型
 * </pre>
 *
 * @author mr.g
 */
@Data
@ConfigurationProperties(prefix = "jpower.ai")
public class AiProperties {

    /**
     * 默认主模型类型（当存在多个模型时使用此模型作为默认）
     * <p>
     * 可选值: deepseek, dashscope, openai, azure-openai, ollama, zhipu
     */
    private ChatModelType primary;
    /**
     * 提示词配置
     */
    private Prompt prompt = new Prompt();

    @Data
    public static class Prompt {
        /**
         * 提示词存储位置
         * <p>
         * 可选值: MYBATIS_FLEX, NACOS
         */
        private PromptLocationType location = PromptLocationType.MYBATIS_FLEX;

        /**
         * 数据库表名
         */
        private String databaseTable = "tb_ai_prompt";
        /**
         * Nacos 配置
         */
        private NacosPrompt nacos = new NacosPrompt();

        @Data
        public static class NacosPrompt {
            /**
             * Nacos 配置分组
             */
            private String group = "AI_PROMPT_GROUP";
            /**
             * Nacos 获取配置超时时间
             */
            private Long timeout = 3000L;
        }
    }

}
