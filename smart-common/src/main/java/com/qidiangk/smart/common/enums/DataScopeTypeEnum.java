package com.qidiangk.smart.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 数据权限类型<br/>
 * <b>该字典框架中使用，请不要随意更改</b>
 *
 * @author mr.g
 */
@AllArgsConstructor
@Getter
public enum DataScopeTypeEnum {

    /**
     * 全部
     **/
    ALL(1, "全部"),
    /**
     * 本人可见
     **/
    OWN(2, "本人可见"),
    /**
     * 所在机构可见
     **/
    OWN_ORG(3, "所在机构可见"),
    /**
     * 所在机构及子级可见
     **/
    OWN_ORG_CHILD(4, "所在机构及子级可见"),
    /**
     * 自定义
     **/
    CUSTOM(5, "自定义");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        DataScopeTypeEnum[] businessModeEnums = values();
        for (DataScopeTypeEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
