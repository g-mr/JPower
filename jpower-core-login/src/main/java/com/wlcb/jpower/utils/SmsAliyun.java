package com.wlcb.jpower.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SmsAliyun
 * @Description TODO 阿里云短信
 * @Author 郭丁志
 * @Date 2020-07-22 10:43
 * @Version 1.0
 */
@Slf4j
public class SmsAliyun {

    public static JSONObject send(String phone, String sign, String code) {
        String accessKeyId = "LTAI4FoKkEuBvZncwXKZuNPG";
        String accessSecret = "4hquq5CQOvFxY5dsCOJA9BrS6vkxnL";
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", sign);
        request.putQueryParameter("TemplateCode", "SMS_196654267");
        request.putQueryParameter("TemplateParam", "{\"code\":\""+code+"\"}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            log.info("短信发送完成：{}",response.getData());
            return JSON.parseObject(response.getData());
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        JSONObject json = new JSONObject();
        json.put("Code","1");
        return json;
    }

    public static void main(String[] args) {
        send("15011071226", "乌兰察布市民卡用户注册", "123456");
    }
}
