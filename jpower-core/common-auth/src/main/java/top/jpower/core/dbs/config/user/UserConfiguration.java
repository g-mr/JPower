package top.jpower.core.dbs.config.user;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.jpower.core.util.user.UserConfig;

/**
 * @author mr.g
 * @date 2025/1/15 21:46
 */
@Configuration
@ConditionalOnMissingBean(UserConfig.class)
public class UserConfiguration {

    @Bean
    UserConfig userConfig() {
        return new DefaultUserConfig();
    }

}
