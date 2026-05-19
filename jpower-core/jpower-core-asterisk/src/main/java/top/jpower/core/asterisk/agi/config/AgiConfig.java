package top.jpower.core.asterisk.agi.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.asteriskjava.fastagi.AgiScript;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import top.jpower.core.asterisk.agi.AGIServer;
import top.jpower.core.asterisk.agi.fastagi.support.AgiHangupEventManager;
import top.jpower.core.asterisk.properties.AsteriskAgiProperties;

import java.util.List;

@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(AsteriskAgiProperties.class)
@ConditionalOnMissingBean(AGIServer.class)
@Slf4j
public class AgiConfig implements SmartLifecycle{

    private AGIServer agiServer;
    private boolean running = false;

    private final List<? extends AgiScript> scriptList;
    private final AsteriskAgiProperties asteriskProperties;

    @Bean
    @ConditionalOnMissingBean
    public AgiHangupEventManager agiHangupEventManager(){
        return new AgiHangupEventManager();
    }

    @Override
    public void start() {
        agiServer = new  AGIServer(scriptList, asteriskProperties);
        // 启动
        agiServer.start();
        running = true;
    }

    @Override
    public void stop() {
        if (agiServer != null) {
            agiServer.shutdown();
        }
        running = false;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

}
