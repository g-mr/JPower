package top.jpower.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.jpower.module.annotation.Menu;

/**
 * 功能类型
 *
 * @author mr.g
 **/
@AllArgsConstructor
@Getter
public enum FunctionTypeEnum {

    /**
     * 按钮
     **/
    BTN(0, "按钮"),
    /**
     * 菜单
     **/
    MENU(1, "菜单"),
    /**
     * 接口
     **/
    INTERFACE(2, "接口");

    private final Integer value;
    private final String name;

    public static String getName(Integer value) {
        FunctionTypeEnum[] businessModeEnums = values();
        for (FunctionTypeEnum businessModeEnum : businessModeEnums) {
            if (businessModeEnum.value.equals(value)) {
                return businessModeEnum.name;
            }
        }
        return null;
    }

    public static FunctionTypeEnum getEnumByType(Menu.TYPE type) {
        switch (type){
            case BTN:
                return BTN;
            case INTERFACE:
                return INTERFACE;
            default:
                return null;
        }
    }

}
