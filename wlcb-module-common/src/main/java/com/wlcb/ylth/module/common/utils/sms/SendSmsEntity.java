package com.wlcb.ylth.module.common.utils.sms;

import com.wlcb.ylth.module.common.utils.constants.ConstantsUtils;
import lombok.Data;

/**
 * @ClassName sms
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-28 22:14
 * @Version 1.0
 */
@Data
public class SendSmsEntity {

    String appKey = ConstantsUtils.APP_KEY;
    String timestamp = "";
    String mobile = "";
    String content = "";
    String sendTime = "";
    String spNumber = "";
    String sign = "";
    String reportUrl = "";
    String moUrl = "";
    Integer customerId=2194;
    String attach = "";

    public SendSmsEntity(String mobiles, String content) {
        this.mobile =mobiles;
        this.content =content;
    }
    public SendSmsEntity(String mobiles, String content,String attach) {
        this.mobile =mobiles;
        this.content =content;
        this.attach = attach;
    }
    public SendSmsEntity() {
    }

}
