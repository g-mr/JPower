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
@FeignClient(value = AppConstant.JPOWER_RESOURCE, fallbackFactory = SmsClientFallback.class, path = "/resource/sms")
public interface SmsClient {

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestDto 请求参数
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSms(@RequestBody SmsRequestDto requestDto);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestSingleDto 请求参数
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSingleSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSingleSms(@RequestBody SmsRequestSingleDto requestSingleDto);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestDto 参数
     * @return boolean
     **/
    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean send(@RequestBody SmsRequestDto requestDto);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param requestSingleDto 参数
     * @return boolean
     **/
    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean sendSingle(@RequestBody SmsRequestSingleDto requestSingleDto);

    /**
     * 给多个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param requestDto 参数
     **/
    @PostMapping(value = "/sendThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendThrow(@RequestBody SmsRequestDto requestDto);

    /**
     * 给一个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param requestSingleDto 参数
     **/
    @PostMapping(value = "/sendSingleThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendSingleThrow(@RequestBody SmsRequestSingleDto requestSingleDto);

    /**
     * 发送验证码
     *
     * @author mr.g
     * @param smsValidateDto 参数
     **/
    @PostMapping(value = "/sendValidate",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean sendValidate(@RequestBody SmsValidateDto smsValidateDto);

    /**
     * 验证码验证
     *
     * @author mr.g
     * @param validateDto 参数
     **/
    @PostMapping(value = "/validate",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean validate(@RequestBody ValidateDto validateDto);
}
