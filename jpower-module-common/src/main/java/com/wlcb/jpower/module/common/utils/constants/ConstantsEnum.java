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

    /**
     * @author 郭丁志
     * @Description //TODO 用户类型
     * @date 0:04 2020/6/2 0002
     * @return
     */

    public static enum USER_TYPE{

        USER_TYPE_SYSTEM(0,"系统用户"),USER_TYPE_GENERAL(1,"普通用户"),USER_TYPE_FIRM(2,"单位用户"),USER_TYPE_MEMBER(3,"会员"),USER_TYPE_ANONYMOUS(9,"匿名用户");

        private USER_TYPE(Integer value, String name){
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
            ConstantsEnum.USER_TYPE[] businessModeEnums = values();
            for (ConstantsEnum.USER_TYPE businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 是否YN
     * @date 18:36 2020/7/26 0026
     * @return
     */
    public static enum YN{

        Y("Y","是"),N("N","否");

        private YN(String value, String name){
            this.value = value;
            this.name = name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(String value) {
            ConstantsEnum.YN[] businessModeEnums = values();
            for (ConstantsEnum.YN businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 是否YN 01
     * @date 18:36 2020/7/26 0026
     * @return
     */
    public static enum YN01{

        Y(1,"是"),N(0,"否");

        private YN01(Integer value, String name){
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
            ConstantsEnum.YN01[] businessModeEnums = values();
            for (ConstantsEnum.YN01 businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    /**
     * @author 郭丁志
     * @Description //TODO 语言种类
     * @date 18:36 2020/7/26 0026
     * @return
     */
    public static enum YYZL{

        CHINA("zh_cn","中文"),ENG("en","英文");

        private YYZL(String value, String name){
            this.value = value;
            this.name = name;
        }
        private final String value;
        private final String name;

        public String getValue() {
            return value;
        }

        public String getName() {
            return name;
        }

        public static String getName(String value) {
            ConstantsEnum.YYZL[] businessModeEnums = values();
            for (ConstantsEnum.YYZL businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

}
