package com.qidiangk.smart.aster.tripartite.property;


import com.alibaba.fastjson.PropertyNamingStrategy;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.annotation.JSONType;
import com.alibaba.nls.client.protocol.InputFormatEnum;
import com.alibaba.nls.client.protocol.OutputFormatEnum;
import com.alibaba.nls.client.protocol.SampleRateEnum;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Data
@Component
@ConfigurationProperties(prefix = "ivr.ali")
public class AliProperty implements Serializable {

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
     * TTS配置
     */
    private TtsOption ttsOption = new TtsOption();

    @Data
    public static class TtsOption implements Serializable {

        /**
         * 设置返回音频的编码格式。
         */
        private OutputFormatEnum format;
        /**
         * 设置返回音频的采样率
         */
        private SampleRateEnum sampleRate;
        /**
         * 音色
         */
        private String voice;
        /**
         * 音量，范围是0~100，可选，默认50。
         */
        private Integer volume;
        /**
         * 语调，范围是-500~500，可选，默认是0。
         */
        private Integer pitchRate;
        /**
         * 语速，范围是-500~500，默认是0。
         */
        private Integer speechRate;
        /**
         * 设置连续两次发送文本的最小时间间隔（毫秒），如果当前调用send时距离上次调用时间小于此值，则会阻塞并等待直到满足条件再发送文本
         */
        private Integer minSendIntervalMs;
    }

    /**
     * ASR配置
     */
    private AsrOption asrOption = new AsrOption();

    @Data
    public static class AsrOption implements Serializable {

        /**
         * 设置音频的编码格式。
         */
        private InputFormatEnum format;
        /**
         * 设置音频的采样率
         */
        private SampleRateEnum sampleRate;
        /**
         * 是否生成并返回标点符号
         */
        private boolean enablePunctuation = true;
        /**
         * 是否将返回结果规整化，比如将一百返回为100。
         */
        private boolean enableItn = true;
        /**
         * 是否返回中间识别结果。
         */
        private boolean enableIntermediateResult = false;
        /**
         * 设置是否语义断句。
         */
        private boolean enableSemanticSentenceDetection = false;
        /**
         * 设置vad断句参数。默认值：800ms，有效值：200ms～6000ms。
         */
        private Integer maxSentenceSilence = 800;
        /**
         * 设置是否开启过滤语气词，即声音顺滑。
         */
        private boolean disfluency = true;
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

    /**
     * 多角色ASR识别配置
     */
    private AsrFileRole asrFileRole = new AsrFileRole();

    @Data
    @JSONType(naming = PropertyNamingStrategy.SnakeCase)
    public static class AsrFileRole implements Serializable {
        /**
         * 文件下载地址
         */
        @JSONField(serialize = false)
        private String fileDomain;
        /**
         * 文件存储路径
         */
        @JSONField(serialize = false)
        private String fileDir = "/data/tran/";
        /**
         * 区域ID
         */
        @JSONField(serialize = false)
        private String regionId = "cn-beijing";
        /**
         * 版本
         * <br/>
         * 中国：2018-08-17
         * <br/>
         * 国际：2019-08-23
         */
        @JSONField(serialize = false)
        private String version = "2018-08-17";
        /**
         * 是否开启返回词信息，默认为false，开启时需要设置version为4.0。
         */
        private Boolean enableWords;
        /**
         * 是否开启智能分轨（开启智能分轨，即可在两方对话的语音情景下，依据每句话识别结果中的ChannelId，判断该句话的发言人为哪一方。通常先发言一方ChannelId为0，8k双声道开启分轨后默认为2个人，声道channel0和channel1就是音轨编号）。
         */
        private Boolean autoSplit;
        /**
         * 大于16 kHz采样率的音频是否进行自动降采样（降为16 kHz），默认为false，开启时需要设置version为4.0。
         */
        private Boolean enableSampleRateAdaptive;
        /**
         * 说话人分离的确定人数方式，需要和auto_split、speaker_num这两个参数搭配使用。
         * <br/>
         * 默认为空：8k由用户指定，16k由算法决定。
         * <br/>
         * 1：用户指定人数，具体人数由参数speaker_num确认。
         * <br/>
         * 2：算法决定人数。
         */
        private Integer superviseType;
        /**
         *
         * 用于辅助指定声纹人数，取值范围为2至100的整数。8k音频默认为2，16k音频默认为100。
         * <br/>
         * 此参数只能辅助算法尽量输出指定人数，无法保证一定会输出此人数。需要和auto_split、supervise_type这两个参数搭配使用。
         */
        private Integer speakerNum;
        /**
         * ITN（逆文本inverse text normalization）中文数字转换阿拉伯数字。设置为True时，中文数字将转为阿拉伯数字输出
         * <br />
         * 默认值：False。
         */
        private Boolean enableInverseTextNormalization;
        /**
         * 过滤语气词，即声音顺滑
         * <br />
         * 默认值false（关闭）
         */
        private Boolean enableDisfluency;
        /**
         * 是否给句子加标点
         * <br />
         * 默认值false（关闭）
         */
        private Boolean enablePunctuationPrediction;
        /**
         * 允许的最大结束静音，取值范围：200~6000，默认值800，单位为毫秒。
         * <br />
         * 开启语义断句enable_semantic_sentence_detection后，此参数无效。
         */
        private Integer maxEndSilence;
        /**
         * 允许单句话最大结束时间，最小值5000，默认值60000。单位为毫秒。
         * <br />
         * 开启语义断句enable_semantic_sentence_detection后，此参数无效。
         */
        private Integer maxSingleSegmentTime;
        /**
         * 是否启⽤语义断句，取值：true/false
         * <br />
         * 默认值false。
         */
        private Boolean enableSemanticSentenceDetection;
        /**
         * 是否启用时间戳校准功能，取值：true/false
         * <br />
         * 默认值false。
         */
        private Boolean enableTimestampAlignment;
        /**
         * 是否只识别首个声道，取值：true/false。（如果录音识别结果重复，您可以开启此参数。）
         * <br />
         * 默认为空：8k处理双声道，16k处理单声道。
         * <br />
         * false：8k处理双声道，16k处理双声道。
         * <br />
         * true：8k处理单声道，16k处理单声道。
         */
        private Boolean firstChannelOnly;
        /**
         * 敏感词过滤功能，支持开启或关闭，支持自定义敏感词。该参数可实现：
         * <br />
         * 不处理（默认，即展示原文）、过滤、替换为*。
         */
        private Boolean specialWordFilter;
        /**
         *
         * 自定义标点断句。
         * <br />
         * 不填默认使用句号、问号、叹号断句。如果用户填写此值，则会增加使用用户指定的标点符号断句。
         * <br />
         * 示例：
         * <br />
         * 按英文逗号断句填写","
         * <br />
         * 按中文和英文逗号断句填写"，,"
         */
        private String punctuationMark;
    }

}
