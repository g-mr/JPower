package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.jpower.exception.JpowerFeignException;

import java.util.List;
import java.util.Map;

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
            public SmsResponse sendSms(String code, Map<String, String> map, List<String> phones) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public SmsResponse sendSingleSms(String code, Map<String, String> map, String phone) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean send(String code, Map<String, String> param, List<String> phones) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendSingle(String code, Map<String, String> param, String phone) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendThrow(String code, Map<String, String> param, List<String> phones) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public void sendSingleThrow(String code, Map<String, String> param, String phone) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean sendValidate(String code, String phone) {
                throw new JpowerFeignException("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public boolean validate(String code, String phone, String phoneCode) {
                throw new JpowerFeignException("短信验证失败==>"+cause.getMessage());
            }
        };
    }
}
