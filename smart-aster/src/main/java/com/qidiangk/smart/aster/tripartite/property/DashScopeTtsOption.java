package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * DashScope TTS 页面动态配置类
 * <br/>
 * 在 IVR 流程的开始节点中配置此项，可覆盖配置文件（ivr.dashscope）中的默认值。
 * <br/>
 * 所有字段均为可选，为 null 时不覆盖配置文件的对应值。
 *
 * @author mr.g
 */
@Data
public class DashScopeTtsOption implements Serializable {

    /**
     * DashScope API Key（覆盖配置文件中的 api-key）
     */
    private String apiKey;

    /**
     * WebSocket 服务地址（支持内网私有化部署）
     * <br/>
     * 示例：wss://dashscope.aliyuncs.com/api-ws/v1/inference
     */
    private String websocketUrl;

    /**
     * TTS 模型名称
     * <br/>
     * CosyVoice 系列：cosyvoice-v3-flash、cosyvoice-v3-plus、cosyvoice-v2、cosyvoice-v1
     * <br/>
     * Qwen-TTS 系列：qwen3-tts-flash-realtime、qwen3-tts-instruct-flash-realtime
     */
    private String model;

    /**
     * 音色名称
     * <br/>
     * CosyVoice：longanyang、longxiaochun 等
     * <br/>
     * Qwen-TTS：Cherry、Ethan 等
     */
    private String voice;

    /**
     * 音量，取值范围 [0, 100]
     */
    private Integer volume;

    /**
     * 语速，取值范围 [0.5, 2.0]
     */
    private Float speechRate;

    /**
     * 语调，取值范围 [0.5, 2.0]
     */
    private Float pitchRate;

}
