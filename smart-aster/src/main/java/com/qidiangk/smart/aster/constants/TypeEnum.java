package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qidiangk.smart.aster.handler.nodes.granter.*;
import com.qidiangk.smart.common.enums.ArrayValuable;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeEnum implements ArrayValuable<TypeEnum> {
    START("start"),
    ANSWER(AnswerGranter.GRANT_TYPE),
    SCRIPT(ScriptGranter.GRANT_TYPE),
    SERVICE(ServiceGranter.GRANT_TYPE),
    CONDITION(ConditionGranter.GRANT_TYPE),
    RECEIVED(ReceivedGranter.GRANT_TYPE),
    TRANSFER(TransferGranter.GRANT_TYPE),
    MAXKB(MaxKBGranter.GRANT_TYPE),
    GLOBE_VALUE(GlobeValueGranter.GRANT_TYPE),
    SAY(SayGranter.GRANT_TYPE),
    SENTIMENT(SentimentGranter.GRANT_TYPE),
    CHILD(ChildGranter.GRANT_TYPE),
    HANGUP(HangupGranter.GRANT_TYPE),
    INTENTION(IntentionGranter.GRANT_TYPE),
    EXTRACT(ExtractGranter.GRANT_TYPE);

    private final String value;

    TypeEnum(String value) {
        this.value = value;
    }

    // 使用标准方法名
    @JsonValue
    public String toValue() {
        return value;
    }

    // 添加大小写不敏感处理
    @JsonCreator
    public static TypeEnum forValue(String value) {
        if (value == null) return null;
        for (TypeEnum type : TypeEnum.values()) {
            if (StrUtil.equalsIgnoreCase(type.value, value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    @Override
    public TypeEnum[] array() {
        return values();
    }

}