package top.jpower.jpower.config.sms;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.core.utils.enums.SmsCategoryEnum;
import top.jpower.core.utils.utils.BeanUtil;
import top.jpower.core.utils.utils.Fc;
import top.jpower.core.utils.utils.GuavaCache;
import top.jpower.jpower.config.sms.properties.AliSmsProperties;
import top.jpower.jpower.dbs.dao.TbResourceSmsDao;
import top.jpower.jpower.dbs.entity.TbResourceSms;
import top.jpower.jpower.module.base.enums.JpowerError;
import top.jpower.jpower.module.base.exception.JpowerAssert;

import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2024/3/6 4:53 PM
 */
@Component
@AllArgsConstructor
public class SmsBuilder {

    private final GuavaCache<SmsTemplate> cache = GuavaCache.getInstance(5L, TimeUnit.MINUTES);
    private TbResourceSmsDao resourceSmsDao;

    /**
     * 获取短信模版
     * @author mr.g
     * @param code 短信编号
     * @return 短信模版
     **/
    public SmsTemplate getTemplate(String code){
        SmsTemplate smsTemplate = cache.get(code);
        if (Fc.notNull(smsTemplate)){
            return smsTemplate;
        }

        TbResourceSms resourceSms = resourceSmsDao.getByCode(code);
        switch (SmsCategoryEnum.getEnum(resourceSms.getCategory())) {
            case ALI:
                smsTemplate = new AliSmsTemplate(BeanUtil.copyProperties(resourceSms, AliSmsProperties.class));
                cache.put(code, smsTemplate);
                return smsTemplate;
            default:
                JpowerAssert.createException(JpowerError.NotFind, "短信发送模版");
                return null;
        }
    }

}
