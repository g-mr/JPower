package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 语言种类
 *
 * @author mr.g
 */
@AllArgsConstructor
@Getter
public enum YYZLEnum {

    /**
     * 中文
     **/
    CHINA("zh-CN", "中文"),
    /**
     * 英文
     **/
    ENG("en-US", "英文");

    private final String value;
    private final String name;

    public static String getName(String value) {
        YYZLEnum[] businessModeEnums = values();
        for (YYZLEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
