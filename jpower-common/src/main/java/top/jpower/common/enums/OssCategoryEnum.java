package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.core.utils.utils.Fc;

import java.util.Arrays;

/**
 * OSS 类型
 *
 * @author mr.g
 **/
@AllArgsConstructor
@Getter
public enum OssCategoryEnum {

    /**
     * 阿里云
     **/
    ALI("ali", "阿里云"),
    /**
     * 七牛云
     **/
    QN("qiniu", "七牛云");

    private final String value;
    private final String name;

    public static String getName(String value) {
        OssCategoryEnum[] businessModeEnums = values();
        for (OssCategoryEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

    public static OssCategoryEnum getEnum(String value) {
        return Arrays.stream(values()).filter(e-> Fc.equalsValue(e.value, value)).findFirst()
                .orElseThrow(()->new NullPointerException("未找到OSS_TYPE="+value));
    }

}
