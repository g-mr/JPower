package top.jpower.resource.service.sms;

import cn.hutool.core.util.PhoneUtil;
import top.jpower.common.constants.CacheNames;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.exception.throwable.JpowerException;
import top.jpower.core.redis.cache.RedisService;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.*;
import top.jpower.resource.service.sms.properties.SmsResponse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static top.jpower.common.constants.ServiceCodeConstants.MOBILE_NOT_LEGAL;
import static top.jpower.common.constants.ServiceCodeConstants.SMS_CODE_SENT;
import static top.jpower.core.util.constants.JpowerConstants.CODE_TEST;

/**
 * 短信发送模板接口
 * <p>
 * 定义短信发送的基本操作和默认实现
 * </p>
 *
 * @author mr.g
 */
public interface SmsTemplate {

    /**
     * 给多个手机号发送短信
     *
     * @author mr.g
     * @param param  参数映射
     * @param phones 手机号码列表
     * @return SmsResponse 发送结果响应
     */
    default SmsResponse sendSms(Map<String, String> param, List<String> phones){
        return sendMessage(param, phones, Boolean.FALSE);
    }

    /**
     * 给一个手机号发送短信
     *
     * @author mr.g
     * @param param 参数映射
     * @param phone 手机号码
     * @return SmsResponse 发送结果响应
     */
    default SmsResponse sendSingleSms(Map<String, String> param, String phone){
        return sendSms(param, Collections.singletonList(phone));
    }

    /**
     * 给多个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param  参数映射
     * @param phones 手机号码列表
     * @throws JpowerException 发送失败异常
     */
    default void sendThrow(Map<String, String> param, List<String> phones) throws JpowerException {
        sendMessage(param, phones, Boolean.TRUE);
    }

    /**
     * 给一个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param 参数映射
     * @param phone 手机号码
     * @throws JpowerException 发送失败异常
     */
    default void sendSingleThrow(Map<String, String> param, String phone) throws JpowerException {
        sendThrow(param, Collections.singletonList(phone));
    }

    /**
     * 给多个手机号发送短信
     *
     * @author mr.g
     * @param param  参数映射
     * @param phones 手机号码列表
     * @return boolean 是否发送成功
     */
    default boolean send(Map<String, String> param, List<String> phones) {
        SmsResponse response = sendMessage(param, phones, Boolean.FALSE);
        return response.isSuccess();
    }

    /**
     * 给一个手机号发送短信
     *
     * @author mr.g
     * @param param 参数映射
     * @param phone 手机号码
     * @return boolean 是否发送成功
     */
    default boolean sendSingle(Map<String, String> param, String phone) {
        return send(param, Collections.singletonList(phone));
    }

    /**
     * 发送短信
     *
     * @author mr.g
     * @param param   参数映射
     * @param phones  手机号码列表
     * @param isThrow 是否抛出异常
     * @return SmsResponse 发送结果响应
     */
    SmsResponse sendMessage(Map<String, String> param, List<String> phones, boolean isThrow);

    /**
     * 发送短信验证码
     *
     * @author mr.g
     * @param phone 手机号码
     * @return boolean 是否发送成功
     */
    default boolean sendValidate(String phone){
        JpowerAssert.isTrue(PhoneUtil.isMobile(phone), JpowerError.Unknown, MOBILE_NOT_LEGAL);
        if (RedisService.getInstance().getExpire(CacheNames.PHONE_KEY+phone, TimeUnit.MINUTES) >= 4){
            JpowerAssert.createException(JpowerError.Business, SMS_CODE_SENT);
        }

        String code = RandomUtil.random6Num();
        boolean is = sendSingle(ChainMap.<String, String>create().put(getParameters().get(0), code).build(), phone);
        if (is){
            RedisService.getInstance().valueOps(String.class).set(CacheNames.PHONE_KEY+phone, code ,5L, TimeUnit.MINUTES);
        }
        return is;
    }

    /**
     * 验证短信验证码是否正确
     *
     * @author mr.g
     * @param phone 手机号码
     * @param code  输入的验证码
     * @return boolean 是否验证正确
     */
    default boolean validate(String phone, String code){
        JpowerProperties jpowerProperties = SpringUtil.getBean(JpowerProperties.class);
        // 获取验证码
        String redisCode = RedisService.getInstance().valueOps(String.class).get(CacheNames.PHONE_KEY + phone);
        // 判断验证码;
        if (Fc.isBlank(code)){
            return Boolean.FALSE;
        }
        if (Fc.notEqualsValue(jpowerProperties.getEnv(), JpowerConstants.DEV_CODE)){
            if (Fc.equalsValue(jpowerProperties.getEnv(), JpowerConstants.TEST_CODE)){
                // 测试环境固定0000
                if (!StringUtil.equalsIgnoreCase(CODE_TEST, code) && !StringUtil.equalsIgnoreCase(redisCode, code)) {
                    return Boolean.FALSE;
                }
            } else {
                if (!StringUtil.equalsIgnoreCase(redisCode, code)) {
                    return Boolean.FALSE;
                }
            }
        }

        return Boolean.TRUE;
    }

    List<String> getParameters();
}
