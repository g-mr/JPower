package top.jpower.core.ai.client;

import lombok.Getter;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.lang.Nullable;

import java.util.List;

/**
 * 可移植的 ChatOptions 实现
 * <p>
 * 提供模型无关的选项配置，支持通用参数和可移植能力（如 JSON 输出模式、联网搜索、深度思考等）。
 * 在请求管道中，{@link ChatClientRequestBuilder} 会自动将此类转换为
 * 与当前 ChatModel 匹配的特定 ChatOptions 类型（如 DashScopeChatOptions、DeepSeekChatOptions 等）。
 * <p>
 * <b>配置继承机制：</b>
 * <ul>
 *   <li>所有字段均为 nullable，null 表示"使用配置文件（application.yml）中的配置值"</li>
 *   <li>非 null 值将覆盖配置文件中的值</li>
 *   <li>转换时以模型默认配置为基底，仅覆盖用户显式设置的字段</li>
 * </ul>
 * <p>
 * 使用示例:
 * <pre>
 * // 仅覆盖 temperature，其他参数（含 DashScope 的 enableSearch 等）继承配置文件
 * chatClients.client()
 *     .prompt()
 *     .options(JpowerChatOptions.builder()
 *         .temperature(0.1)
 *         .maxTokens(600)
 *         .build())
 *     .user("分析这段文本")
 *     .call().content();
 *
 * // 显式覆盖 DashScope 特有参数
 * chatClients.client()
 *     .prompt()
 *     .options(JpowerChatOptions.builder()
 *         .temperature(0.1)
 *         .enableSearch(true)
 *         .enableThinking(true)
 *         .jsonMode()
 *         .build())
 *     .user("搜索并分析")
 *     .call().content();
 * </pre>
 *
 * @author mr.g
 */
@Getter
public class JpowerChatOptions implements ChatOptions {

    /**
     * 响应格式类型（可移植的 responseFormat 抽象）
     */
    public enum ResponseType {
        /**
         * JSON 格式输出
         * <p>
         * 映射关系：
         * <ul>
         *   <li>DashScope → DashScopeResponseFormat(type="json_object")</li>
         *   <li>OpenAI/DeepSeek → ResponseFormat(type=JSON_OBJECT)</li>
         *   <li>Ollama → format="json"</li>
         * </ul>
         */
        JSON,
        /**
         * 纯文本格式输出（默认）
         */
        TEXT
    }

    // ================ 通用参数（ChatOptions 标准接口字段） ================

    @Nullable
    private final String model;
    @Nullable
    private final Double temperature;
    @Nullable
    private final Integer maxTokens;
    @Nullable
    private final Double topP;
    @Nullable
    private final Integer topK;
    @Nullable
    private final Double frequencyPenalty;
    @Nullable
    private final Double presencePenalty;
    @Nullable
    private final List<String> stopSequences;

    // ================ 可移植能力参数 ================

    /**
     * 可移植的响应格式类型
     * <p>
     * 设置后，管道会自动转换为当前模型对应的 responseFormat 实现。
     * null 表示不设置（保留配置文件默认值）。
     */
    @Nullable
    private final ResponseType responseType;

    /**
     * 启用联网搜索
     * <p>
     * 映射：支持的模型（DashScope） → enableSearch
     * <br>
     * null 表示继承配置文件中的值。
     */
    @Nullable
    private final Boolean enableSearch;

    /**
     * 启用深度思考（推理模式）
     * <p>
     * 映射：支持的模型（DashScope） → enableThinking
     * <br>
     * null 表示继承配置文件中的值。
     */
    @Nullable
    private final Boolean enableThinking;

    /**
     * 启用多模态处理
     * <p>
     * 映射：支持的模型（DashScope） → multiModel
     * <br>
     * null 表示继承配置文件中的值。
     */
    @Nullable
    private final Boolean multiModel;

    /**
     * 启用增量输出（流式场景）
     * <p>
     * 映射：支持的模型（DashScope） → incrementalOutput
     * <br>
     * null 表示继承配置文件中的值。
     */
    @Nullable
    private final Boolean incrementalOutput;

    private JpowerChatOptions(Builder builder) {
        this.model = builder.model;
        this.temperature = builder.temperature;
        this.maxTokens = builder.maxTokens;
        this.topP = builder.topP;
        this.topK = builder.topK;
        this.frequencyPenalty = builder.frequencyPenalty;
        this.presencePenalty = builder.presencePenalty;
        this.stopSequences = builder.stopSequences;
        this.responseType = builder.responseType;
        this.enableSearch = builder.enableSearch;
        this.enableThinking = builder.enableThinking;
        this.multiModel = builder.multiModel;
        this.incrementalOutput = builder.incrementalOutput;
    }

    @Override
    public ChatOptions copy() {
        return new JpowerChatOptions(Builder.from(this));
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String model;
        private Double temperature;
        private Integer maxTokens;
        private Double topP;
        private Integer topK;
        private Double frequencyPenalty;
        private Double presencePenalty;
        private List<String> stopSequences;
        private ResponseType responseType;
        private Boolean enableSearch;
        private Boolean enableThinking;
        private Boolean multiModel;
        private Boolean incrementalOutput;

        public Builder model(String model) {
            this.model = model;
            return this;
        }

        public Builder temperature(Double temperature) {
            this.temperature = temperature;
            return this;
        }

        public Builder maxTokens(Integer maxTokens) {
            this.maxTokens = maxTokens;
            return this;
        }

        public Builder topP(Double topP) {
            this.topP = topP;
            return this;
        }

        public Builder topK(Integer topK) {
            this.topK = topK;
            return this;
        }

        public Builder frequencyPenalty(Double frequencyPenalty) {
            this.frequencyPenalty = frequencyPenalty;
            return this;
        }

        public Builder presencePenalty(Double presencePenalty) {
            this.presencePenalty = presencePenalty;
            return this;
        }

        public Builder stopSequences(List<String> stopSequences) {
            this.stopSequences = stopSequences;
            return this;
        }

        /**
         * 设置可移植的响应格式类型
         * <p>
         * 管道会根据实际模型类型自动转换为对应的 responseFormat 实现。
         */
        public Builder responseType(ResponseType responseType) {
            this.responseType = responseType;
            return this;
        }

        /**
         * 快捷方法：启用 JSON 输出模式
         */
        public Builder jsonMode() {
            this.responseType = ResponseType.JSON;
            return this;
        }

        /**
         * 启用联网搜索（DashScope 特性）
         * <p>
         * 设置为 true/false 将覆盖配置文件值，不调用此方法则继承配置文件值。
         */
        public Builder enableSearch(Boolean enableSearch) {
            this.enableSearch = enableSearch;
            return this;
        }

        /**
         * 启用深度思考/推理模式（DashScope 特性）
         * <p>
         * 设置为 true/false 将覆盖配置文件值，不调用此方法则继承配置文件值。
         */
        public Builder enableThinking(Boolean enableThinking) {
            this.enableThinking = enableThinking;
            return this;
        }

        /**
         * 启用多模态模式（DashScope 特性）
         * <p>
         * 设置为 true/false 将覆盖配置文件值，不调用此方法则继承配置文件值。
         */
        public Builder multiModel(Boolean multiModel) {
            this.multiModel = multiModel;
            return this;
        }

        /**
         * 启用增量输出（DashScope 流式特性）
         * <p>
         * 设置为 true/false 将覆盖配置文件值，不调用此方法则继承配置文件值。
         */
        public Builder incrementalOutput(Boolean incrementalOutput) {
            this.incrementalOutput = incrementalOutput;
            return this;
        }

        public JpowerChatOptions build() {
            return new JpowerChatOptions(this);
        }

        static Builder from(JpowerChatOptions source) {
            Builder b = new Builder();
            b.model = source.model;
            b.temperature = source.temperature;
            b.maxTokens = source.maxTokens;
            b.topP = source.topP;
            b.topK = source.topK;
            b.frequencyPenalty = source.frequencyPenalty;
            b.presencePenalty = source.presencePenalty;
            b.stopSequences = source.stopSequences;
            b.responseType = source.responseType;
            b.enableSearch = source.enableSearch;
            b.enableThinking = source.enableThinking;
            b.multiModel = source.multiModel;
            b.incrementalOutput = source.incrementalOutput;
            return b;
        }
    }
}
