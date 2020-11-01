package com.wlcb.jpower.utils;

import com.alibaba.fastjson.JSONObject;
import com.wlcb.jpower.module.base.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName SmsUtil
 * @Description TODO 短信实现工具类
 * @Author 郭丁志
 * @Date 2020-07-22 10:43
 * @Version 1.0
 */
@Slf4j
public class SmsUtil {

    public static JSONObject send(String phone, String code) {
        //请在这里实现发送短信的业务即可
        throw new BusinessException("暂未实现手机号发送");
    }

}
