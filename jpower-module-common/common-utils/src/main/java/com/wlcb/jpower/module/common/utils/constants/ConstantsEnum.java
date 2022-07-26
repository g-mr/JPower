package com.wlcb.jpower.module.common.utils.constants;

/**
 * 字典 枚举
 *
 * @author mr.g
 **/
public class ConstantsEnum {


    /**
     * @Author 郭丁志
     * @Description //TODO 证件类型
     * @Date 16:14 2020-05-19
     **/
    public enum ID_TYPE {

        ID_CARD(1, "身份证"), CHINESE_PASSPORT(2, "中国护照"), MTP_S(3, "台胞证"), FOREIGN_PASSPORT(4, "外国护照"), PERMANENT_RESIDENCE_PERMIT(5, "外国人永久居住证");

        ID_TYPE(Integer value, String name) {
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
            ID_TYPE[] businessModeEnums = values();
            for (ID_TYPE businessModeEnum : businessModeEnums) {
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
    public enum ACTIVATION_STATUS {

        ACTIVATION_YES(1, "激活"), ACTIVATION_NO(0, "未激活");

        ACTIVATION_STATUS(Integer value, String name) {
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
            ACTIVATION_STATUS[] businessModeEnums = values();
            for (ACTIVATION_STATUS businessModeEnum : businessModeEnums) {
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

    public enum USER_TYPE {

        USER_TYPE_SYSTEM(0, "系统用户"),
        USER_TYPE_GENERAL(1, "普通用户"),
        USER_TYPE_FIRM(2, "单位用户"),
        USER_TYPE_MEMBER(3, "会员"),
        USER_TYPE_CUSTOMER(4, "客服用户"),
        USER_TYPE_ANONYMOUS(9, "匿名用户");

        USER_TYPE(Integer value, String name) {
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
            USER_TYPE[] businessModeEnums = values();
            for (USER_TYPE businessModeEnum : businessModeEnums) {
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
    public enum YN {

        Y("Y", "是"), N("N", "否");

        YN(String value, String name) {
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
            YN[] businessModeEnums = values();
            for (YN businessModeEnum : businessModeEnums) {
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
    public enum YN01 {

        Y(1, "是"), N(0, "否");

        YN01(Integer value, String name) {
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
            YN01[] businessModeEnums = values();
            for (YN01 businessModeEnum : businessModeEnums) {
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
    public enum YYZL {

        CHINA("zh_cn", "中文"), ENG("en", "英文");

        YYZL(String value, String name) {
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
            YYZL[] businessModeEnums = values();
            for (YYZL businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    public enum DATA_SCOPE_TYPE {

        ALL(1, "全部"), OWN(2, "本人可见"), OWN_ORG(3, "所在机构可见"), OWN_ORG_CHILD(4, "所在机构及子级可见"), CUSTOM(5, "自定义");

        DATA_SCOPE_TYPE(Integer value, String name) {
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
            DATA_SCOPE_TYPE[] businessModeEnums = values();
            for (DATA_SCOPE_TYPE businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

    /**
     * 文件存储位置
     * @Author mr.g
     **/
    public enum FILE_STORAGE_TYPE {

        SERVER("SERVER", "服务器"), FASTDFS("FASTDFS", "fastdfs"), DATABASE("DATABASE", "数据库");

        FILE_STORAGE_TYPE(String value, String name) {
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
            FILE_STORAGE_TYPE[] businessModeEnums = values();
            for (FILE_STORAGE_TYPE businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

}
