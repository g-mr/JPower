package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户类型
 *
 * @author mr.g
 * @date 16:14 2020-05-19
 **/
@AllArgsConstructor
@Getter
public enum UserTypeEnum {

    /**
     * 系统用户
     **/
    USER_TYPE_SYSTEM(0, "系统用户"),
    /**
     * 普通用户
     **/
    USER_TYPE_GENERAL(1, "普通用户"),
    /**
     * 单位用户
     **/
    USER_TYPE_FIRM(2, "单位用户"),
    /**
     * 会员
     **/
    USER_TYPE_MEMBER(3, "会员"),
    /**
     * 客服用户
     **/
    USER_TYPE_CUSTOMER(4, "客服用户"),
    /**
     * 匿名用户
     * <b> 系统内置用户类型最好别对其进行修改 </b>
     **/
    USER_TYPE_ANONYMOUS(9, "匿名用户");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        UserTypeEnum[] businessModeEnums = values();
        for (UserTypeEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

}
