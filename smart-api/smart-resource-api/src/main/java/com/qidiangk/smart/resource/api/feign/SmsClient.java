package com.qidiangk.smart.resource.api.feign;

import com.qidiangk.smart.resource.api.dto.SmsValidateDTO;
import com.qidiangk.smart.resource.api.dto.ValidateDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.qidiangk.smart.common.constants.AppConstant;
import top.jpower.core.util.rsp.R;

/**
 * 短信客户端
 *
 * @author mr.g
 */
@FeignClient(value = AppConstant.JPOWER_RESOURCE, fallbackFactory = SmsClientFallback.class, path = "/feign/resource/sms")
public interface SmsClient {

    /**
     * 发送验证码
     *
     * @author mr.g
     * @param smsValidateDTO 参数
     **/
    @PostMapping(value = "/sendValidate",produces =  MediaType.APPLICATION_JSON_VALUE)
	R<Boolean> sendValidate(@RequestBody SmsValidateDTO smsValidateDTO);

    /**
     * 验证码验证
     *
     * @author mr.g
     * @param validateDTO 参数
     **/
    @PostMapping(value = "/validate",produces =  MediaType.APPLICATION_JSON_VALUE)
	R<Boolean> validate(@RequestBody ValidateDTO validateDTO);
}
