package com.wlcb.wlj.module.common.utils.constants;

/**
 * @ClassName ConstantsUtils
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-02-03 18:07
 * @Version 1.0
 */
public class ConstantsEnum {
    
    /**
     * @Author 郭丁志
     * @Description //TODO 企业联系人关联审核状态
     * @Date 18:24 2020-02-03
     * @Param 
     * @return 
     **/
    public static enum APPLICANT_STATUS{

        SUCCESS(1,"申请成功"),FAIL(2,"申请失败"),REVIEW(3,"审核中"),MATCH(4,"匹配成功");

        private APPLICANT_STATUS(Integer value,String name){
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
            APPLICANT_STATUS[] businessModeEnums = values();
            for (APPLICANT_STATUS businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

}
