package com.qidiangk.smart.aster.tripartite.property;

import lombok.Data;

import java.io.Serializable;

/**
 * 阿里ASR配置类
 *
 * @author mr.g
 */
@Data
public class AliAsrOption implements Serializable {

    /**
     * accessKeyId
     */
    private String accessKeyId;
    /**
     * accessKeySecret
     */
    private String accessKeySecret;

    /**
     * appKey
     */
    private String appKey;

    /**
     * 设置是否语义断句。
     */
    private Boolean enableSemanticSentenceDetection;
    /**
     * 设置vad断句参数。默认值：800ms，有效值：200ms～6000ms。
     */
    private Integer maxSentenceSilence;
    /**
     * 设置是否开启过滤语气词，即声音顺滑。
     */
    private Boolean disfluency;
    /**
     * 设置是否开启热词模式。
     */
    private Boolean enableWords;
    /**
     * 设置vad噪音阈值参数，参数取值为-1～+1，如-0.9、-0.8、0.2、0.9。
     * 取值越趋于-1，判定为语音的概率越大，亦即有可能更多噪声被当成语音被误识别。
     * 取值越趋于+1，判定为噪音的越多，亦即有可能更多语音段被当成噪音被拒绝识别。
     * 该参数属高级参数，调整需慎重和重点测试。
     */
    private Double speechNoiseThreshold;
    /**
     * 训练后的定制语言模型id
     */
    private String customizationId;
    /**
     * 训练后的定制热词id
     */
    private String vocabularyId;
}
