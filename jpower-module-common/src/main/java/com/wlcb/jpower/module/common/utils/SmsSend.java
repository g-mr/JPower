package com.wlcb.jpower.module.common.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.common.utils.constants.ConstantsUtils;
import com.wlcb.jpower.module.common.utils.sms.SendSmsEntity;

import java.net.URLEncoder;

/**
 * @ClassName SmsSend
 * @Description TODO 短信发送入口
 * @Author 郭丁志
 * @Date 2020-03-28 22:12
 * @Version 1.0
 */
public class SmsSend {

    /**
     * @Author 郭丁志
     * @Description //TODO 拼接短信发送参数
     * @Date 22:44 2020-03-28
     * @Param [mobiles, content]
     * @return com.wlcb.jpower.module.common.utils.sms.SendSmsEntity
     **/
    private static SendSmsEntity buildSendEntity(String mobiles, String content) {
        // TODO Auto-generated method stub
        SendSmsEntity entity = new SendSmsEntity(mobiles,content);
        entity.setAppKey(ConstantsUtils.APP_KEY);
        entity.setTimestamp(System.currentTimeMillis() + "");
        entity.setSpNumber("");
        String source = entity.getAppKey() + entity.getTimestamp() + entity.getMobile() + entity.getContent()
                + entity.getSpNumber() + entity.getSendTime() + ConstantsUtils.APP_SECRET;
        String sign = MD5.parseStrToMd5L32(source);
        entity.setSign(sign);
        return entity;
    }

    /**
     * @Author 郭丁志
     * @Description //TODO 发送短信
     * @Date 22:44 2020-03-28
     * @Param [phone, content]
     * @return com.alibaba.fastjson.JSONObject
     **/
    public static JSONObject sendLiantong(String phone, String content) {
        JSONObject res = new JSONObject();
        try {
            SendSmsEntity sendSmsEntity = buildSendEntity(phone, content);
            String data = JSON.toJSONString(sendSmsEntity);
            String encodeData = URLEncoder.encode(data, "UTF-8");
            String respose = HttpClient.doPostJson(ConstantsUtils.SMS_SEND_URL,encodeData);
            res = JSON.parseObject(respose);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        JSONObject a = sendLiantong("15011071226","您的健康验证码:123456，如非本人操作，请忽略本短信！");
        System.out.println(a);
    }

}
