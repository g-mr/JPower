package com.wlcb.jpower.module.common.utils.constants;

import org.apache.commons.lang3.StringUtils;

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
     * @Description //TODO 审核状态
     * @Date 18:24 2020-02-03
     * @Param
     * @return
     **/
    public static enum AUDIT_STATUS{

        AGREE(1,"审核通过"),REFUSE(2,"审核拒绝"),PROCESSING(3,"审核中");

        private AUDIT_STATUS(Integer value,String name){
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
            AUDIT_STATUS[] businessModeEnums = values();
            for (AUDIT_STATUS businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }

        /**
         * @Author 郭丁志
         * @Description //TODO 是否包含key
         * @Date 12:50 2020-04-09
         * @Param [value]
         * @return boolean
         **/
        public static boolean containsKey(Integer key) {

            boolean isContains = false;

            AUDIT_STATUS[] businessModeEnums = values();
            for (AUDIT_STATUS businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(key)) {
                    isContains = true;
                    break;
                }
            }
            return isContains;
        }
    }

    
    /**
     * @Author 郭丁志
     * @Description //TODO 企业联系人关联审核状态
     * @Date 18:24 2020-02-03
     * @Param 
     * @return 
     **/
    public static enum APPLICANT_STATUS{

        SUCCESS(1,"申请成功"),FAIL(2,"申请失败"),REVIEW(3,"审核中"),MATCH(4,"匹配成功"),CONFIRM(5,"法人确认");

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

    /**
     * @Author 郭丁志
     * @Description //TODO 微信红包返回的错误代码
     * @Date 19:28 2020-03-21
     **/
    public static enum WECHAR_RED_CODE{

        SUCCESS("SUCCESS","成功"),FAIL("FAIL","发送失败"),NO_AUTH("NO_AUTH","没有该接口权限"),AMOUNT_LIMIT("AMOUNT_LIMIT","金额超限"),PARAM_ERROR("PARAM_ERROR","参数错误"),
        OPENID_ERROR("OPENID_ERROR","Openid错误"),SEND_FAILED("SEND_FAILED","付款错误"),NOTENOUGH("NOTENOUGH","余额不足"),SYSTEMERROR("SYSTEMERROR","系统繁忙，请稍后再试"),
        NAME_MISMATCH("NAME_MISMATCH","姓名校验出错"),SIGN_ERROR("SIGN_ERROR","签名错误"),XML_ERROR("XML_ERROR","Post内容出错"),FATAL_ERROR("FATAL_ERROR","两次请求参数不一致"),FREQ_LIMIT("FREQ_LIMIT","超过频率限制，请稍后再试"),MONEY_LIMIT("MONEY_LIMIT","已经达到今日付款总额上限/已达到付款给此用户额度上限"),CA_ERROR("CA_ERROR","API证书校验出错"),
        V2_ACCOUNT_SIMPLE_BAN("V2_ACCOUNT_SIMPLE_BAN","无法给未实名用户付款"),PARAM_IS_NOT_UTF8("PARAM_IS_NOT_UTF8","请求参数中包含非utf8编码字符"),SENDNUM_LIMIT("SENDNUM_LIMIT","该用户今日付款次数超过限制"),RECV_ACCOUNT_NOT_ALLOWED("RECV_ACCOUNT_NOT_ALLOWED","收款账户不在收款账户列表"),
        PAY_CHANNEL_NOT_ALLOWED("PAY_CHANNEL_NOT_ALLOWED","本商户号未配置API发起能力");

        private WECHAR_RED_CODE(String value, String name){
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
            WECHAR_RED_CODE[] businessModeEnums = values();
            for (WECHAR_RED_CODE businessModeEnum : businessModeEnums) {
                if (businessModeEnum.value.equals(value)) {
                    return businessModeEnum.name;
                }
            }
            return null;
        }
    }

}
