package com.wlcb.jpower.module.dictbind;

import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.config.interceptor.JpowerMybatisInterceptor;
import com.wlcb.jpower.module.dictbind.handler.IDictBindHandler;
import com.wlcb.jpower.module.dictbind.interceptor.DictBindInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author mr.g
 * @Date 2021/11/16 0016 21:33
 */
@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore({JpowerMybatisInterceptor.class})
public class DictBindConfig {

    @Bean
    @ConditionalOnProperty(value = {"jpower.dictbind.enable"}, matchIfMissing = true)
    @ConditionalOnMissingBean({DictBindInterceptor.class})
    public DictBindInterceptor dictBindInterceptor(@Autowired(required = false) IDictBindHandler dictBindHandler) {
        if (Fc.isNull(dictBindHandler)){
            return null;
        }
        DictBindInterceptor interceptor = new DictBindInterceptor();
        interceptor.setDictBindHandler(dictBindHandler);
        return interceptor;
    }

}
