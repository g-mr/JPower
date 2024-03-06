package top.jpower.jpower.config.sms;

import lombok.AllArgsConstructor;
import lombok.Getter;
import top.jpower.jpower.config.sms.properties.AliSmsProperties;
import top.jpower.jpower.dto.SmsResponse;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/3/6 11:42 AM
 */
@AllArgsConstructor
public class AliSmsTemplate implements SmsTemplate {

    @Getter
    private final AliSmsProperties properties;

    /**
     * 发送短信
     *
     * @param param   参数
     * @param phones  手机号
     * @param isThrow 是否要抛出异常
     * @return 发送结果
     * @author mr.g
     **/
    @Override
    public SmsResponse sendMessage(Map<String, String> param, List<String> phones, boolean isThrow) {
        System.out.println(properties);

        return null;
    }
}
