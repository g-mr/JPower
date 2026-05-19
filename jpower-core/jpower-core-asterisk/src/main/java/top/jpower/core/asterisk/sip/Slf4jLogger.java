package top.jpower.core.asterisk.sip;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "SIP")
public class Slf4jLogger implements Logger {
    @Override
    public final void debug(String message) {
        log.debug(message);
    }

    @Override
    public final void info(String message) {
        log.info(message);
    }

    @Override
    public final void error(String message) {
        log.error(message);
    }

    @Override
    public final void error(String message, Exception exception) {
        log.error(message);
        exception.printStackTrace();
    }

    @Override
    public final void traceNetwork(String message, String direction) {
        log.info("[{}]\n{}", direction, message);
    }

}
