package top.jpower.core.dbs.dictbind;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import top.jpower.core.dbs.config.interceptor.JpowerMybatisInterceptor;
import top.jpower.core.dbs.dictbind.handler.IDictBindHandler;
import top.jpower.core.dbs.dictbind.interceptor.DictBindInterceptor;

/**
 * @Author mr.g
 * @Date 2021/11/16 0016 21:33
 */
@AutoConfiguration
@AutoConfigureBefore({JpowerMybatisInterceptor.class})
@ConditionalOnProperty(value = {"jpower.dictbind.enable"}, matchIfMissing = true)
@ConditionalOnBean(IDictBindHandler.class)
public class DictBindConfig {

    @Bean
    @ConditionalOnMissingBean({DictBindInterceptor.class})
    public DictBindInterceptor dictBindInterceptor(IDictBindHandler dictBindHandler) {
        DictBindInterceptor interceptor = new DictBindInterceptor();
        interceptor.setDictBindHandler(dictBindHandler);
        return interceptor;
    }

}
