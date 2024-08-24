package top.jpower.jpower.config.sms;

import com.alibaba.fastjson2.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import top.jpower.core.util.constants.ReturnConstants;
import top.jpower.core.util.constants.StringPool;
import top.jpower.core.util.utils.Fc;
import top.jpower.jpower.config.sms.properties.AliSmsProperties;
import top.jpower.jpower.dto.SmsResponse;
import top.jpower.core.exception.throwable.JpowerException;

import java.util.List;
import java.util.Map;

/**
 * @author mr.g
 * @date 2024/3/6 11:42 AM
 */
@Slf4j
public class AliSmsTemplate implements SmsTemplate {

    @Getter
    private final AliSmsProperties properties;
    @Getter
    private final List<String> parameters;

    private final Client client;

    public AliSmsTemplate(AliSmsProperties properties){
        this.properties = properties;
        this.parameters = Fc.toStrList(properties.getParameters());
        client = createClient();
    }

    /**
     * 使用AK&SK初始化账号Client
     * @return Client
     */
    @SneakyThrows
    private Client createClient() {
        Config config = new Config()
                .setEndpoint("dysmsapi.aliyuncs.com")
                .setRegionId(properties.getRegionId())
                .setAccessKeyId(properties.getAccessKey())
                .setAccessKeySecret(properties.getSecretKey());
        return new Client(config);
    }

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
        SendSmsRequest sendSmsRequest = new SendSmsRequest()
                .setSignName(properties.getSign())
                .setTemplateCode(properties.getTemplate())
                .setPhoneNumbers(Fc.join(phones))
                .setTemplateParam(JSON.toJSONString(param));
        try {
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, new RuntimeOptions());
            if (Fc.equalsValue(sendSmsResponse.statusCode, ReturnConstants.RECODE_SUCCESS) && Fc.equalsValue(sendSmsResponse.body.code, StringPool.OK)){
                return new SmsResponse(Boolean.TRUE, sendSmsResponse.statusCode, sendSmsResponse.body.message);
            } else {
                log.error("短信发送失败==>>{}", JSON.toJSONString(sendSmsResponse));
                if (isThrow){
                    throw new JpowerException(sendSmsResponse.body.message);
                }
                return new SmsResponse(Boolean.FALSE, sendSmsResponse.statusCode, sendSmsResponse.body.message);
            }
        } catch (Exception error){
            log.error("短信发送失败==>>{}", error.getMessage());
            if (isThrow){
                throw new JpowerException(error.getMessage());
            }
            return new SmsResponse(Boolean.FALSE, ReturnConstants.RECODE_SYSTEM, error.getMessage());
        }
    }

}
