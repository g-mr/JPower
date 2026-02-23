package top.jpower.resource.service.sms;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import top.jpower.common.enums.SmsCategoryEnum;
import top.jpower.core.exception.enums.JpowerError;
import top.jpower.core.exception.throwable.JpowerAssert;
import top.jpower.core.util.utils.BeanUtil;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.util.utils.GuavaCache;
import top.jpower.resource.dbs.dao.ResourceSmsDao;
import top.jpower.resource.dbs.entity.ResourceSms;
import top.jpower.resource.service.sms.properties.AliSmsProperties;
import top.jpower.resource.service.sms.storage.AliSmsTemplate;

import java.util.concurrent.TimeUnit;

import static top.jpower.common.constants.ServiceCodeConstants.NOT_FOUND_SMS_TEMPLATE;

/**
 * 短信构建器
 *
 * @author mr.g
 */
@Component
@AllArgsConstructor
public class SmsBuilder {

    private final GuavaCache<SmsTemplate> cache = GuavaCache.getInstance(5L, TimeUnit.MINUTES);
    private ResourceSmsDao resourceSmsDao;

    /**
     * 获取短信模板
     *
     * @param code 短信编码
     * @return SmsTemplate 短信模板
     * @author mr.g
     * @since 2024-03-06
     */
    public SmsTemplate getTemplate(String code){
        SmsTemplate smsTemplate = cache.get(code);
        if (Fc.notNull(smsTemplate)){
            return smsTemplate;
        }

        ResourceSms resourceSms = resourceSmsDao.getByCode(code);
        switch (SmsCategoryEnum.getEnum(resourceSms.getCategory())) {
            case ALI:
                smsTemplate = new AliSmsTemplate(BeanUtil.copyProperties(resourceSms, AliSmsProperties.class));
                cache.put(code, smsTemplate);
                return smsTemplate;
            default:
                JpowerAssert.createException(JpowerError.NotFind, NOT_FOUND_SMS_TEMPLATE);
                return null;
        }
    }

}
