package top.jpower.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 安全验证
 *
 * @author mr.g
 */
@Component
@EnableWebSecurity
public class SecuritySecureConfig {

    private final String adminContextPath;

    public SecuritySecureConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl(adminContextPath + "/");

        http.authorizeHttpRequests(auth ->
                auth.dispatcherTypeMatchers(DispatcherType.ASYNC, DispatcherType.FORWARD).permitAll()
                .requestMatchers(adminContextPath + "/actuator/**").permitAll()
                .requestMatchers(adminContextPath + "/login").permitAll()
                .requestMatchers(adminContextPath + "/assets/**").permitAll()
                .requestMatchers(adminContextPath + "/instances/**").permitAll()
                .requestMatchers(adminContextPath + "/applications/**").permitAll()
                .requestMatchers(adminContextPath + "/notifications/**").permitAll()
                .requestMatchers(adminContextPath + "/journal/**").permitAll()
                .requestMatchers(adminContextPath + "/extensions/**").permitAll()
                .requestMatchers(adminContextPath + "/events/**").permitAll()
                //必须对每个其他请求进行身份验证
                .anyRequest().authenticated())
                .formLogin(formLogin ->formLogin.loginPage(adminContextPath + "/login").successHandler(successHandler))
                .logout(logout -> logout.logoutUrl(adminContextPath + "/logout"))
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

}
