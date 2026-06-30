package com.qidiangk.smart.aster.tripartite.property;


import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONType;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "ivr.dianxin")
public class DianxinProperty {

    /**
     * appId
     */
    private String appId;
    /**
     * appKey
     */
    private String appKey;
    /**
     * ttsUrl
     */
    private String ttsUrl;
    /**
     * 电信TTS配置
     */
    private TtsOption ttsOption = new TtsOption();
    /**
     * asrUrl
     */
    private String asrUrl;

    /**
     * 电信ASR配置
     */
    private AsrOption asrOption = new AsrOption();

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class TtsOption implements Serializable {
        /**
         * 音频编码格式，支持 PCM 格式，默认值：PCM
         */
        private String format;
        /**
         * 音频采样率，支持 22050Hz、16000 Hz 和 8000 Hz
         */
        private Integer sampleRate = 8000;
        /**
         * 说话人，支持音色 cixingnan、songchinan、surennan、cixingnan2、zhubonan、kefunan、zhuchinan、jilunan2、surennv、xianliaonv、 mansunv、kefunv1、huoponv、ruyanv、xinwennv、kefunv2、luolinv、huoponv2
         */
        private String voice;
        /**
         * 音量，取值范围：[0, 100]，默认值：50
         */
        private Integer volume;

        // **************** 以下不是配置项，写在这纯属是为了方便转换 ********************

        /**
         * 请求ID，记录该值便于排查问题
         */
        private String reqId;
        /**
         * 待合成的文本，需要为 UTF-8 编码
         */
        private String text;
    }

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class AsrOption implements Serializable {
        /**
         * 音频采样率
         */
        private Integer sampleRate = 8000;
        /**
         * 	是否加标点，默认：是
         */
        private Boolean enablePunctuation;
        /**
         * 是否开启 ITN，默认：是
         */
        private Boolean enableInverseTextNormalization = false;
        /**
         * 是否开启校勘（仅对多方言服务有效），默认：否
         */
        private Boolean enableEmendation;
        /**
         * 是否开启返回词信息，默认：否
         */
        private Boolean enableWords;
        /**
         * 热词列表
         */
        private List<String> hotwords;
        /**
         * 句尾静音阈值，单位ms，最小值为10，最大无限制
         */
        private Integer maxEndSilence;
    }
}
