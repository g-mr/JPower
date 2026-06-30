package com.qidiangk.smart.resource.controller.api;

import com.qidiangk.smart.resource.api.dto.SmsValidateDTO;
import com.qidiangk.smart.resource.api.dto.ValidateDTO;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.jpower.core.util.rsp.R;
import com.qidiangk.smart.resource.api.feign.SmsClient;
import com.qidiangk.smart.resource.service.sms.SmsBuilder;

/**
 * @author mr.g
 * @date 2024/3/6 6:05 PM
 */
@Hidden
@RestController
@RequestMapping("/feign/resource/sms")
@RequiredArgsConstructor
public class SmsClientController implements SmsClient {

    private final SmsBuilder smsBuilder;

	@Override
	@PostMapping(value = "/sendValidate",produces =  MediaType.APPLICATION_JSON_VALUE)
	public R<Boolean> sendValidate(@RequestBody SmsValidateDTO smsValidateDto) {
		smsBuilder.getTemplate(smsValidateDto.getCode()).sendValidate(smsValidateDto.getPhone());
		return R.status(true);
	}

    @Override
    @PostMapping(value = "/validate",produces =  MediaType.APPLICATION_JSON_VALUE)
    public R<Boolean> validate(@RequestBody ValidateDTO validateDto) {
        return R.status(smsBuilder.getTemplate(validateDto.getCode()).validate(validateDto.getPhone(), validateDto.getPhoneCode()));
    }
}
