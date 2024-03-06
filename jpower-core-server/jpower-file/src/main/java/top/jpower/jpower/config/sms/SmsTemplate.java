package top.jpower.jpower.config.sms;

import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.module.base.exception.JpowerException;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 短信发送模板
 *
 * @author mr.g
 */
public interface SmsTemplate {

    /**
     * 给多个手机号发送短信
     *
     * @author mr.g
     * @param param 参数
     * @param phones 手机号
     * @return 发送结果
     **/
    default SmsResponse sendSms(Map<String, String> param, List<String> phones){
        return sendMessage(param, phones, Boolean.FALSE);
    }

    /**
     * 给一个手机号发送短信
     *
     * @author mr.g
     * @param param 参数
     * @param phone 手机号
     * @return 发送结果
     **/
    default SmsResponse sendSingleSms(Map<String, String> param, String phone){
        return sendSms(param, Collections.singletonList(phone));
    }

    /**
     * 给多个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param 参数
     * @param phones 手机号
     **/
    default void sendThrow(Map<String, String> param, List<String> phones) throws JpowerException {
        sendMessage(param, phones, Boolean.TRUE);
    }

    /**
     * 给一个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param 参数
     * @param phone 手机号
     **/
    default void sendSingleThrow(Map<String, String> param, String phone) throws JpowerException {
        sendThrow(param, Collections.singletonList(phone));
    }

    /**
     * 给多个手机号发送短信
     *
     * @author mr.g
     * @param param 参数
     * @param phones 手机号
     * @return 是否成功
     **/
    default boolean send(Map<String, String> param, List<String> phones) {
        SmsResponse response = sendMessage(param, phones, Boolean.FALSE);
        return response.isSuccess();
    }

    /**
     * 给一个手机号发送短信
     *
     * @author mr.g
     * @param param 参数
     * @param phone 手机号
     * @return 是否成功
     **/
    default boolean sendSingle(Map<String, String> param, String phone) {
        return send(param, Collections.singletonList(phone));
    }

    /**
     * 发送短信
     *
     * @author mr.g
     * @param param 参数
     * @param phones 手机号
     * @param isThrow 是否要抛出异常
     * @return 发送结果
     **/
    SmsResponse sendMessage(Map<String, String> param, List<String> phones, boolean isThrow);

    Object getProperties();
}
