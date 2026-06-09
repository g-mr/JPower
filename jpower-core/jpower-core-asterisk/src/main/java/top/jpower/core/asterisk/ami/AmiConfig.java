package top.jpower.core.asterisk.ami;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.ClassUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.live.AsteriskServer;
import org.asteriskjava.live.DefaultAsteriskServer;
import org.asteriskjava.manager.DefaultManagerConnection;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerEventListener;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import top.jpower.core.asterisk.ami.annotation.AmiListener;
import top.jpower.core.asterisk.properties.AsteriskAmiProperties;
import top.jpower.core.util.utils.Fc;

import java.util.Arrays;
import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(AsteriskAmiProperties.class)
@ConditionalOnMissingBean(ManagerConnection.class)
@Slf4j
public class AmiConfig {

    @Bean
    public ManagerConnection managerConnection(AsteriskAmiProperties amiProperties, List<ManagerEventListener> listenerList) {

        ManagerConnection managerConnection = new DefaultManagerConnection(amiProperties.getHost(), amiProperties.getPort(), amiProperties.getUsername(), amiProperties.getPassword());

        if (Fc.isNotEmpty(listenerList)){
            managerConnection.addEventListener(event -> {
                listenerList.forEach(managerEventListener -> {
                    AmiListener amiListener = AnnotationUtil.getAnnotation(managerEventListener.getClass(), AmiListener.class);
                    try {
                        if (Fc.isNull(amiListener) || Fc.isEmpty(amiListener.value())){
                            managerEventListener.onManagerEvent(event);
                        } else {
                            if (Arrays.stream(amiListener.value()).anyMatch(clz-> ClassUtil.isAssignable(clz, event.getClass()))){
                                managerEventListener.onManagerEvent(event);
                            }
                        }
                    } catch (Exception e) {
                        log.error("Asterisk监听事件异常===>>", e);
                    }

                });

            });
        }

        try {
            managerConnection.login();
        } catch (Exception e){
            log.error("AMI 登录异常===>>{}:{} {} {} \n {}", amiProperties.getHost(), amiProperties.getPort(), amiProperties.getUsername(), amiProperties.getPassword(), ExceptionUtil.stacktraceToString(e));
        }

        return managerConnection;
    }

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnBean(ManagerConnection.class)
    public AsteriskServer asteriskServer(ManagerConnection managerConnection) {
        return new DefaultAsteriskServer(managerConnection);
    }

}
