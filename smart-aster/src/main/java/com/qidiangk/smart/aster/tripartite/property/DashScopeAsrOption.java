package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * DashScope ASR（Qwen-ASR-Realtime）页面动态配置类
 * <br/>
 * 在 IVR 流程的开始节点中配置此项，可覆盖配置文件（ivr.dashscope）中的默认值。
 * <br/>
 * 所有字段均为可选，为 null 时不覆盖配置文件的对应值。
 *
 * @author mr.g
 */
@Data
public class DashScopeAsrOption implements Serializable {

    /**
     * DashScope API Key（覆盖配置文件中的 api-key）
     */
    private String apiKey;

    /**
     * WebSocket 服务地址（支持内网私有化部署）
     * <br/>
     * 示例：wss://dashscope.aliyuncs.com/api-ws/v1/realtime
     */
    private String websocketUrl;

    /**
     * ASR 模型名称
     * <br/>
     * 可选值：qwen3-asr-flash-realtime、 fun-asr-realtime、paraformer-realtime-v2、paraformer-realtime-8k-v2
     */
    private String model;

    /**
     * VAD 断句：句尾最大静音时长（ms）
     * <br/>
     * 有效值：200 ~ 6000
     */
    private Integer maxEndSilence;


    // **************** 以下是Fun模型的配置 ********************


    /**
     * 是否开启语义标点
     */
    private Boolean semanticPunctuationEnabled;

    /**
     * 自定义热词表 ID（对应 DashScope SDK 中的 phraseId）
     * <br/>
     * 在 DashScope 控制台创建热词表后，填入 ID 可提升特定词汇的识别准确率。
     */
    private String phraseId;


    // **************** 以下是Qwen模型的配置 ********************


    /**
     * 音频源语言
     * <br/>
     * 可选值：zh、en、ja 等，参见官方文档
     */
    private String language;

    /**
     * VAD 检测阈值，取值范围 [-1, 1]
     */
    private Float turnDetectionThreshold;
}
