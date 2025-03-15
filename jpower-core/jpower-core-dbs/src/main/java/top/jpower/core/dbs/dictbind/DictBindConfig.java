package top.jpower.core.dbs.dictbind;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.util.utils.Fc;
import top.jpower.core.dbs.dictbind.handler.IDictBindHandler;
import top.jpower.core.dbs.dictbind.interceptor.DictBindInterceptor;

/**
 * @Author mr.g
 * @Date 2021/11/16 0016 21:33
 */
@AutoConfiguration
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
