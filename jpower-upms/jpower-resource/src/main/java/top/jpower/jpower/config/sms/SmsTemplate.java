package top.jpower.jpower.config.sms;

import cn.hutool.core.util.PhoneUtil;
import org.apache.commons.lang3.RandomStringUtils;
import top.jpower.core.util.constants.JpowerConstants;
import top.jpower.core.util.utils.ChainMap;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.SpringUtil;
import top.jpower.core.util.utils.StringUtil;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;
import top.jpower.jpower.module.base.exception.JpowerException;
import top.jpower.jpower.module.common.cache.CacheNames;
import top.jpower.jpower.module.common.deploy.props.JpowerProperties;
import top.jpower.jpower.module.common.redis.RedisUtil;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static top.jpower.core.util.constants.JpowerConstants.CODE_TEST;

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

    /**
     * 发送短信验证码
     *
     * @author mr.g
     * @param phone 手机号
     * @return 是否发送成功
     **/
    default boolean sendValidate(String phone){
        JpowerAssert.isTrue(PhoneUtil.isMobile(phone), JpowerError.Unknown, "手机号不合法");
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        if (redisUtil.getExpire(CacheNames.PHONE_KEY+phone, TimeUnit.MINUTES) >= 4){
            JpowerAssert.createException(JpowerError.Business, "该验证码已经发送，请一分钟后重试");
        }

        String code = RandomStringUtils.randomNumeric(6);
        boolean is = sendSingle(ChainMap.<String, String>create().put(getParameters().get(0), code).build(), phone);
        if (is){
            redisUtil.set(CacheNames.PHONE_KEY+phone, code ,5L, TimeUnit.MINUTES);
        }
        return is;
    }

    /**
     * 验证短信验证码是否正确
     *
     * @author mr.g
     * @param phone 手机号
     * @param code 输入的验证码
     * @return 是否正确
     **/
    default boolean validate(String phone, String code){
        RedisUtil redisUtil = SpringUtil.getBean(RedisUtil.class);
        JpowerProperties jpowerProperties = SpringUtil.getBean(JpowerProperties.class);
        // 获取验证码
        String redisCode = String.valueOf(redisUtil.get(CacheNames.PHONE_KEY + phone));
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
