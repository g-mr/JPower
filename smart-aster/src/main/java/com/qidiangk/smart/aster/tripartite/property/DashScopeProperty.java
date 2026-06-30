package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * DashScope Qwen-ASR-Realtime（Qwen3-ASR）配置类
 * <br/>
 * 配置前缀：ivr.dashscope
 * <br/>
 * 注意：页面流程中配置的 asrOption 会动态覆盖此处配置，参见 {@link DashScopeAsrOption}
 *
 * @author mr.g
 */
@Data
@Component
@ConfigurationProperties(prefix = "ivr.dashscope")
public class DashScopeProperty implements Serializable {

    /**
     * DashScope API Key
     * <br/>
     * 建议通过环境变量 DASHSCOPE_API_KEY 配置，此处可写 ${DASHSCOPE_API_KEY:}
     */
    private String apiKey;

    /**
     * 自定义 WebSocket 服务地址（支持内网私有化部署）
     * <br/>
     * 留空则使用阿里云官方默认地址：wss://dashscope.aliyuncs.com/api-ws/v1/realtime
     * <br/>
     * 不同地域的 URL 不同，海外（新加坡）：wss://dashscope-intl.aliyuncs.com/api-ws/v1/realtime
     */
    private String websocketUrl;

    /**
     * ASR 配置
     */
    private AsrOption asrOption = new AsrOption();

    /**
     * TTS 配置
     */
    private TtsOption ttsOption = new TtsOption();

    @Data
    public static class AsrOption implements Serializable {

        /**
         * 使用的 ASR 模型名称
         * <br/>
         * 可选值：
         * <ul>
         *   <li>qwen3-asr-flash-realtime（默认）</li>
         * </ul>
         */
        private String model = "qwen3-asr-flash-realtime";

        /**
         * 音频采样率（Hz）
         * <br/>
         * 可选值：8000（电话场景）、16000
         * <br/>
         * 电话 VoIP 场景建议使用 8000
         */
        private Integer sampleRate = 8000;

        /**
         * 音频编码格式
         * <br/>
         * 可选值：pcm、wav（默认）、opus
         */
        private String format = "wav";

        /**
         * VAD 断句静音阈值（ms）
         * <br/>
         * 推荐值：400，默认 800，取值范围 [200, 6000]
         */
        private Integer maxEndSilence = 800;

        /**
         * 音频源语言
         * <br/>
         * 默认值：zh（中文）
         */
        private String language = "zh";

        /**
         * VAD 检测阈值
         * <br/>
         * 取值范围 [-1, 1]
         */
        private Float turnDetectionThreshold = 0.2f;


        // ***************** 以下是 Qwen模型 配置项  ******************

        /**
         * 是否开启服务端 VAD 断句
         * <br/>
         * 默认 true。关闭后需手动调用 commit() 触发识别，本实现暂不支持手动模式。
         */
        private Boolean enableTurnDetection = true;

        /**
         * 上下文文本（热词/语境偏置）
         * <br/>
         * 提供背景文本、实体词汇或参考材料以提升识别准确率，最大 10000 tokens
         */
        private String corpusText;


        // ***************** 以下是 Fun模型 配置项  ******************

        /**
         * 是否开启语义标点
         * <br/>
         * 开启后根据语义自动添加标点符号，关闭则仅按静音 VAD 断句
         */
        private Boolean semanticPunctuationEnabled = false;

        /**
         * 自定义热词表 ID（对应 DashScope SDK 中的 phraseId）
         * <br/>
         * 在 DashScope 控制台创建热词表后，填入 ID 可提升特定词汇的识别准确率。
         * <br/>
         * SDK 层面字段名为 phraseId
         */
        private String phraseId;
    }

    @Data
    public static class TtsOption implements Serializable {

        /**
         * 使用的 TTS 模型名称
         * <br/>
         * CosyVoice 系列可选值：
         * <ul>
         *   <li>cosyvoice-v3-flash（推荐，实时性最好）</li>
         *   <li>cosyvoice-v3-plus</li>
         *   <li>cosyvoice-v2</li>
         *   <li>cosyvoice-v1</li>
         * </ul>
         * Qwen-TTS 系列可选值：
         * <ul>
         *   <li>qwen3-tts-flash-realtime</li>
         *   <li>qwen3-tts-instruct-flash-realtime</li>
         * </ul>
         */
        private String model = "qwen3-tts-flash-realtime";

        /**
         * 音色名称
         * <br/>
         * CosyVoice 系列音色请参考官方音色列表，常用值：longanyang、longxiaochun 等
         * <br/>
         * Qwen-TTS 系列常用值：Cherry、Ethan 等
         */
        private String voice;

        /**
         * 音频采样率（Hz）
         * <br/>
         * 可选值：8000（电话场景）、16000、22050、24000、44100、48000
         * <br/>
         * 电话 VoIP 场景建议使用 8000
         */
        private Integer sampleRate = 8000;

        /**
         * 音量
         * <br/>
         * 取值范围：[0, 100]，默认 50
         */
        private Integer volume = 50;

        /**
         * 语速
         * <br/>
         * CosyVoice 取值范围：[0.5, 2.0]，默认 1.0
         * <br/>
         * Qwen-TTS 取值范围：[0.5, 2.0]，默认 1.0
         */
        private Float speechRate = 1.0f;

        /**
         * 语调
         * <br/>
         * CosyVoice 取值范围：[0.5, 2.0]，默认 1.0
         * <br/>
         * Qwen-TTS 取值范围：[0.5, 2.0]，默认 1.0
         */
        private Float pitchRate = 1.0f;

    }
}
