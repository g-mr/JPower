package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dto.*;
import top.jpower.jpower.exception.JpowerFeignException;

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
            public SmsResponse sendSms(SmsRequestDto requestDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public SmsResponse sendSingleSms(SmsRequestSingleDto requestSingleDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean send(SmsRequestDto requestDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendSingle(SmsRequestSingleDto requestSingleDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendThrow(SmsRequestDto requestDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendSingleThrow(SmsRequestSingleDto requestSingleDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendValidate(SmsValidateDto smsValidateDto) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean validate(ValidateDto validateDto) {
                throw new JpowerFeignException("短信验证失败==>"+cause.getMessage());
            }
        };
    }
}
