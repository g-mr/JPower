package top.jpower.core.exception.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.exception.aspectj.OperateLogAspect;
import top.jpower.core.exception.listener.ErrorLogListener;
import top.jpower.core.exception.listener.OperateLogListener;
import top.jpower.core.exception.model.UserDto;
import top.jpower.core.util.utils.Fc;

/**
 * 日志工具配置
 *
 * @Author mr.g
 * @Date 2021/5/1 0001 0:16
 */
@AutoConfiguration
@ConditionalOnWebApplication
public class JpowerLogConfiguration {

    @Bean
    public OperateLogAspect apiLogAspect(@Autowired(required = false) UserConfig userConfig) {
        return new OperateLogAspect(Fc.notNull(userConfig)?userConfig.queryUser():new UserDto());
    }

    @Bean
    @ConditionalOnMissingBean(name = "operateLogListener")
    public OperateLogListener operateLogListener(JpowerProperties jpowerProperties) {
        return new OperateLogListener(jpowerProperties);
    }

    @Bean
    @ConditionalOnMissingBean(name = "errorLogListener")
    public ErrorLogListener errorLogListener(JpowerProperties jpowerProperties) {
        return new ErrorLogListener(jpowerProperties);
    }

    // todo 这里有问题 如果没引入 auth模块 就报错
    // @Bean
    // @ConditionalOnMissingBean
    // @ConditionalOnClass(LoginUserContext.class)
    // public UserConfig errorLogListener() {
    //     return new DefaultUserConfig();
    // }
}
