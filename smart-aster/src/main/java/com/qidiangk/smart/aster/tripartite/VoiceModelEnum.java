package com.qidiangk.smart.aster.tripartite;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qidiangk.smart.aster.tripartite.ali.AliAsrClient;
import com.qidiangk.smart.aster.tripartite.ali.AliTtsClient;
import com.qidiangk.smart.aster.tripartite.dashscope.DashScopeAsrClient;
import com.qidiangk.smart.aster.tripartite.dashscope.DashScopeTtsClient;
import com.qidiangk.smart.aster.tripartite.dianxin.DianxinAsrClient;
import com.qidiangk.smart.aster.tripartite.dianxin.DianxinTtsClient;
import com.qidiangk.smart.common.enums.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.core.asterisk.audio.AsrClient;
import top.jpower.core.asterisk.audio.TtsClient;

/**
 * 语音模型 enum
 *
 * @author mr.g
 */
@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum VoiceModelEnum implements ArrayValuable<VoiceModelEnum> {


    ALI("ali", "阿里云服务", AliAsrClient.class, AliTtsClient.class),
    DIANXIN("dianxin", "电信服务", DianxinAsrClient.class, DianxinTtsClient.class),
    DASHSCOPE("dashscope", "通义千问", DashScopeAsrClient.class, DashScopeTtsClient.class);

    @JsonValue
    private String code;
    private String desc;
    private Class<? extends AsrClient> asrClientClass;
    private Class<? extends TtsClient> ttsClientClass;

    /**
     * @return 数组
     */
    @Override
    public VoiceModelEnum[] array() {
        return values();
    }
}
