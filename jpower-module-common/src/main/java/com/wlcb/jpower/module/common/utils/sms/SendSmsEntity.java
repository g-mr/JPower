package com.wlcb.jpower.module.common.utils.sms;

import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
/**
 * @ClassName sms
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020-03-28 22:14
 * @Version 1.0
 */
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

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getSpNumber() {
        return spNumber;
    }

    public void setSpNumber(String spNumber) {
        this.spNumber = spNumber;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReportUrl() {
        return reportUrl;
    }

    public void setReportUrl(String reportUrl) {
        this.reportUrl = reportUrl;
    }

    public String getMoUrl() {
        return moUrl;
    }

    public void setMoUrl(String moUrl) {
        this.moUrl = moUrl;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }
}
