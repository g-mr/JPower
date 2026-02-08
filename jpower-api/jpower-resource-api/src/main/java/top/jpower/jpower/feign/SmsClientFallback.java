package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.core.feign.exception.JpowerFeignException;
import top.jpower.jpower.dto.*;

/**
 * @author mr.g
 * @date 2024/3/6 5:56 PM
 */
@Component
@Slf4j
public class SmsClientFallback implements FallbackFactory<SmsClient> {
    @Override
    public SmsClient create(Throwable cause) {
        return new SmsClient() {

            @Override
            public SmsResponse sendSms(SmsRequestDTO requestDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public SmsResponse sendSingleSms(SmsRequestSingleDTO requestSingleDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean send(SmsRequestDTO requestDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendSingle(SmsRequestSingleDTO requestSingleDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendThrow(SmsRequestDTO requestDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendSingleThrow(SmsRequestSingleDTO requestSingleDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendValidate(SmsValidateDTO smsValidateDTO) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean validate(ValidateDTO validateDTO) {
                throw new JpowerFeignException("短信验证失败==>"+cause.getMessage());
            }
        };
    }
}
