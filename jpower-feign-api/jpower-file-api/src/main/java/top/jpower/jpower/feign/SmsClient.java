package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/3/6 5:54 PM
 */
@FeignClient(value = AppConstant.JPOWER_FILE, fallbackFactory = SmsClientFallback.class, path = "/resource/sms")
public interface SmsClient {

    /**
     * 发送短信
     *
     * @author mr.g
     * @param code 编号
     * @param param 参数
     * @param phones 手机号
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSms(@RequestParam String code, @RequestParam Map<String,String> param, @RequestParam List<String> phones);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param code 编号
     * @param param 参数
     * @param phone 手机号
     * @return SmsResponse
     **/
    @PostMapping(value = "/sendSingleSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    SmsResponse sendSingleSms(@RequestParam String code,@RequestParam Map<String,String> param,@RequestParam String phone);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param code 编号
     * @param param 参数
     * @param phones 手机号
     * @return boolean
     **/
    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean send(@RequestParam String code,@RequestParam Map<String, String> param,@RequestParam List<String> phones);

    /**
     * 发送短信
     *
     * @author mr.g
     * @param code 编号
     * @param param 参数
     * @param phone 手机号
     * @return boolean
     **/
    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    boolean sendSingle(@RequestParam String code,@RequestParam Map<String, String> param,@RequestParam String phone);

    /**
     * 给多个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param 参数
     * @param phones 手机号
     **/
    @PostMapping(value = "/sendThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendThrow(@RequestParam String code,@RequestParam Map<String, String> param,@RequestParam List<String> phones);

    /**
     * 给一个手机号发送短信，发送失败抛出异常
     *
     * @author mr.g
     * @param param 参数
     * @param phone 手机号
     **/
    @PostMapping(value = "/sendSingleThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    void sendSingleThrow(@RequestParam String code,@RequestParam Map<String, String> param,@RequestParam String phone);
}
