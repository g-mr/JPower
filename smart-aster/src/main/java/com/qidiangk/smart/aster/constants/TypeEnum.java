package com.qidiangk.smart.aster.constants;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import com.qidiangk.smart.common.enums.ArrayValuable;
import com.qidiangk.smart.aster.handler.nodes.granter.ExtractGranter;
import com.qidiangk.smart.aster.handler.nodes.granter.IntentionGranter;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TypeEnum implements ArrayValuable<TypeEnum> {
    ANSWER("answer"),
    SCRIPT("script"),
    SERVICE("service"),
    CONDITION("condition"),
    RECEIVED("received"),
    TRANSFER("transfer"),
    MAXKB("maxKB"),
    GLOBE_VALUE("variable-assign"),
    SAY("say"),
    SENTIMENT("sentiment"),
    CHILD("childNodes"),
    HANGUP("hangup"),
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