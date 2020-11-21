package com.wlcb.jpower.log.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.wlcb.jpower.log.utils.ElkPropsUtil;
import com.wlcb.jpower.module.common.utils.StringUtil;

/**
 * @author ding
 * @description
 * @date 2020-11-19 11:28
 */
public class LoggerStartupListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    public LoggerStartupListener() {
    }

    @Override
    public void start() {
        Context context = this.getContext();
        context.putProperty("ELK_MODE", "FALSE");
        context.putProperty("LOG_APPENDER", "log");
        context.putProperty("ERROR_APPENDER", "error");
        context.putProperty("DESTINATION", "127.0.0.1:9000");
        String destination = ElkPropsUtil.getDestination();
        if (StringUtil.isNotBlank(destination)) {
            context.putProperty("ELK_MODE", "TRUE");
            context.putProperty("LOG_APPENDER", "log_logstash");
            context.putProperty("ERROR_APPENDER", "error_logstash");
            context.putProperty("DESTINATION", destination);
        }

    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext context) {
    }

    @Override
    public void onReset(LoggerContext context) {
    }

    @Override
    public void onStop(LoggerContext context) {
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }
}
