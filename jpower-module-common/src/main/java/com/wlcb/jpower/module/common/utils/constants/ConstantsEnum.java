package com.wlcb.jpower.module.common.utils.constants;

/**
 * @ClassName ConstantsUtils
 * @Description TODO 字典
 * @Author 郭丁志
 * @Date 2020-02-03 18:07
 * @Version 1.0
 */
public class ConstantsEnum {


    /**
     * @Author 郭丁志
     * @Description //TODO 证件类型
     * @Date 16:14 2020-05-19
     **/
    public static enum ID_TYPE{

        ID_CARD(1,"身份证"),CHINESE_PASSPORT(2,"中国护照"),MTP_S(3,"台胞证"),FOREIGN_PASSPORT(4,"外国护照"),PERMANENT_RESIDENCE_PERMIT(5,"外国人永久居住证");

        private ID_TYPE(Integer value, String name){
            this.value = value;
            this.name = name;
        }
        private final Integer value;
        private final String name;

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(Integer value) {
            ConstantsEnum.ID_TYPE[] businessModeEnums = values();
            for (ConstantsEnum.ID_TYPE businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 激活状态
     * @Date 16:14 2020-05-19
     **/
    public static enum ACTIVATION_STATUS{

        ACTIVATION_YES(1,"激活"),ACTIVATION_NO(0,"未激活");

        private ACTIVATION_STATUS(Integer value, String name){
            this.value = value;
            this.name = name;
        }
        private final Integer value;
        private final String name;

        public Integer getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(Integer value) {
            ConstantsEnum.ACTIVATION_STATUS[] businessModeEnums = values();
            for (ConstantsEnum.ACTIVATION_STATUS businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }


}
