package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 是否YN
 *
 * @author mr.g
 */
@AllArgsConstructor
@Getter
public enum YN01Enum {

    /**
     * 是
     **/
    Y(1, "是"),
    /**
     * 否
     **/
    N(0, "否");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        YN01Enum[] businessModeEnums = values();
        for (YN01Enum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

    public static boolean isExist(Integer key) {
        for (YN01Enum businessModeEnum : values()) {
            if (businessModeEnum.value.equals(key)) {
                return true;
            }
        }
        return false;
    }

}
