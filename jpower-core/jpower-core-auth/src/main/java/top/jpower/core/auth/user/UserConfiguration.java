package top.jpower.core.auth.user;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import top.jpower.core.util.user.UserConfig;

/**
 * @author mr.g
 * @date 2025/1/15 21:46
 */
@AutoConfiguration
@ConditionalOnMissingBean(UserConfig.class)
public class UserConfiguration {

    @Bean
    UserConfig userConfig() {
        return new DefaultUserConfig();
    }

}
