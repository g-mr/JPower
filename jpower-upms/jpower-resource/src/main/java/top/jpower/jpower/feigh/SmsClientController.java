package top.jpower.jpower.feigh;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import top.jpower.jpower.config.sms.SmsBuilder;
import top.jpower.jpower.dto.*;
import top.jpower.resource.api.dto.SmsResponse;
import top.jpower.resource.api.dto.SmsValidateDTO;
import top.jpower.resource.api.feign.SmsClient;

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
    @PostMapping(value = "/sendSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    public SmsResponse sendSms(@RequestBody SmsRequestDto requestDto) {
        return smsBuilder.getTemplate(requestDto.getCode()).sendSms(requestDto.getMap(), requestDto.getPhones());
    }

    @Override
    @PostMapping(value = "/sendSingleSms",produces =  MediaType.APPLICATION_JSON_VALUE)
    public SmsResponse sendSingleSms(@RequestBody SmsRequestSingleDto requestSingleDto) {
        return smsBuilder.getTemplate(requestSingleDto.getCode()).sendSingleSms(requestSingleDto.getMap(), requestSingleDto.getPhone());
    }

    @Override
    @PostMapping(value = "/send",produces =  MediaType.APPLICATION_JSON_VALUE)
    public boolean send(@RequestBody SmsRequestDto requestDto) {
        return smsBuilder.getTemplate(requestDto.getCode()).send(requestDto.getMap(), requestDto.getPhones());
    }

    @Override
    @PostMapping(value = "/sendSingle",produces =  MediaType.APPLICATION_JSON_VALUE)
    public boolean sendSingle(@RequestBody SmsRequestSingleDto requestSingleDto) {
        return smsBuilder.getTemplate(requestSingleDto.getCode()).sendSingle(requestSingleDto.getMap(), requestSingleDto.getPhone());
    }

    @Override
    @PostMapping(value = "/sendThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    public void sendThrow(@RequestBody SmsRequestDto requestDto) {
        smsBuilder.getTemplate(requestDto.getCode()).sendThrow(requestDto.getMap(), requestDto.getPhones());
    }

    @Override
    @PostMapping(value = "/sendSingleThrow",produces =  MediaType.APPLICATION_JSON_VALUE)
    public void sendSingleThrow(@RequestBody SmsRequestSingleDto requestSingleDto) {
        smsBuilder.getTemplate(requestSingleDto.getCode()).sendSingleThrow(requestSingleDto.getMap(), requestSingleDto.getPhone());
    }

    @Override
    @PostMapping(value = "/sendValidate",produces =  MediaType.APPLICATION_JSON_VALUE)
    public boolean sendValidate(@RequestBody SmsValidateDTO smsValidateDto) {
        return smsBuilder.getTemplate(smsValidateDto.getCode()).sendValidate(smsValidateDto.getPhone());
    }

    @Override
    @PostMapping(value = "/validate",produces =  MediaType.APPLICATION_JSON_VALUE)
    public boolean validate(@RequestBody ValidateDto validateDto) {
        return smsBuilder.getTemplate(validateDto.getCode()).validate(validateDto.getPhone(), validateDto.getPhoneCode());
    }
}
