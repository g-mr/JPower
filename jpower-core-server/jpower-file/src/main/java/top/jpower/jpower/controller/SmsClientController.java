package top.jpower.jpower.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.config.sms.SmsBuilder;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.feign.SmsClient;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/3/6 6:05 PM
 */
@ApiIgnore
@RestController
@RequestMapping("/resource/sms")
@RequiredArgsConstructor
public class SmsClientController implements SmsClient {

    private final SmsBuilder smsBuilder;

    @Override
    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<SmsResponse> send(@RequestParam String code, @RequestParam Map<String,String> map, @RequestParam List<String> phones) {
        return ReturnJsonUtil.data(smsBuilder.getTemplate(code).sendSms(map, phones));
    }

    @Override
    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    public ResponseData<SmsResponse> sendSingle(@RequestParam String code, @RequestParam Map<String,String> map, @RequestParam String phone) {
        return ReturnJsonUtil.data(smsBuilder.getTemplate(code).sendSingleSms(map, phone));
    }
}
