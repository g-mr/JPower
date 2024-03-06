package top.jpower.jpower.feign;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import top.jpower.jpower.module.base.vo.ResponseData;
import top.jpower.jpower.module.common.utils.ReturnJsonUtil;

import java.util.Collections;
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
            public ResponseData send(String code, Map<String, String> map, List<String> phones) {
                return ReturnJsonUtil.fail("短信发送失败==>"+cause.getMessage());
            }

            @Override
            public ResponseData sendSingle(String code, Map<String, String> map, String phone) {
                return send(code, map, Collections.singletonList(phone));
            }
        };
    }
}
