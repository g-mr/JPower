package com.qidiangk.smart.resource.api.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.rsp.R;
import com.qidiangk.smart.resource.api.dto.SmsValidateDTO;
import com.qidiangk.smart.resource.api.dto.ValidateDTO;

import static top.jpower.core.util.constants.ReturnConstants.RECODE_SYSTEM;

/**
 * @author mr.g
 */
@Slf4j
@Component
public class SmsClientFallback implements FallbackFactory<SmsClient> {
    @Override
    public SmsClient create(Throwable cause) {
        return new SmsClient() {

            @Override
            public R<Boolean> sendValidate(SmsValidateDTO smsValidateDTO) {
				JpowerAssert.createException(JpowerError.Rpc, RECODE_SYSTEM, cause.getMessage());
                return R.fail("短信发送失败");
            }

            @Override
            public R<Boolean> validate(ValidateDTO validateDTO) {
				JpowerAssert.createException(JpowerError.Rpc, RECODE_SYSTEM, cause.getMessage());
				return R.fail("短信验证失败");
            }
        };
    }
}
