package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.jpower.common.constants.AppConstant;
import top.jpower.jpower.dto.*;

/**
 * @author mr.g
 * @date 2024/3/6 5:54 PM
 */
@FeignClient(value = AppConstant.JPOWER_RESOURCE, fallbackFactory = SmsClientFallback.class, path = "/feign/resource/sms")
public interface SmsClient {

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestDTO 请求参数
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSms(@RequestBody SmsRequestDTO requestDTO);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestSingleDTO 请求参数
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSingleSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSingleSms(@RequestBody SmsRequestSingleDTO requestSingleDTO);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestDTO 参数
     * @return boolean
     **/
    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean send(@RequestBody SmsRequestDTO requestDTO);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestSingleDTO 参数
     * @return boolean
     **/
    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean sendSingle(@RequestBody SmsRequestSingleDTO requestSingleDTO);

    /**
     * 给多个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param requestDTO 参数
     **/
    @PostMapping(value = "/sendThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendThrow(@RequestBody SmsRequestDTO requestDTO);

    /**
     * 给一个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param requestSingleDTO 参数
     **/
    @PostMapping(value = "/sendSingleThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendSingleThrow(@RequestBody SmsRequestSingleDTO requestSingleDTO);

    /**
     * 发送验证码
     *
     * @author mr.g
     * @param smsValidateDTO 参数
     **/
    @PostMapping(value = "/sendValidate",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean sendValidate(@RequestBody SmsValidateDTO smsValidateDTO);

    /**
     * 验证码验证
     *
     * @author mr.g
     * @param validateDTO 参数
     **/
    @PostMapping(value = "/validate",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean validate(@RequestBody ValidateDTO validateDTO);
}
