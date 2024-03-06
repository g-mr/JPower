package top.jpower.jpower.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.constants.AppConstant;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/3/6 5:54 PM
 */
@FeignClient(value = AppConstant.JPOWER_FILE, fallbackFactory = SmsClientFallback.class, path = "/resource/sms")
public interface SmsClient {

    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    ResponseData<SmsResponse> send(@RequestParam String code, @RequestParam Map<String,String> map, @RequestParam List<String> phones);

    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    ResponseData<SmsResponse> sendSingle(@RequestParam String code,@RequestParam Map<String,String> map,@RequestParam String phone);

}
