package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 证件类型
 *
 * @author mr.g
 * @date 16:14 2020-05-19
 **/
@AllArgsConstructor
@Getter
public enum IdTypeEnum {

    /**
     * 身份证
     **/
    ID_CARD(1, "身份证"),
    /**
     * 中国护照
     **/
    CHINESE_PASSPORT(2, "中国护照"),
    /**
     * 台胞证
     **/
    MTP_S(3, "台胞证"),
    /**
     * 外国护照
     **/
    FOREIGN_PASSPORT(4, "外国护照"),
    /**
     * 外国人永久居住证
     **/
    PERMANENT_RESIDENCE_PERMIT(5, "外国人永久居住证");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        IdTypeEnum[] businessModeEnums = values();
        for (IdTypeEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
