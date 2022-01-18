package com.wlcb.jpower.log.listener;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.LogbackException;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;
import com.wlcb.jpower.log.property.LogProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.StringUtil;

import java.util.Properties;

/**
 * @Author mr.g
 * @Date 2022/1/17 0017 21:32
 */
public class LoggerStartupListener extends ContextAwareBase
        implements LoggerContextListener, LifeCycle {

    protected boolean started = false;

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

    @Override
    public void start() {
        if (started) {
            return;
        }

        Context context = getContext();
        if (StringUtil.contains(context.getProperty("mode"),LogProperties.LogGenre.elk.name()) && Fc.isBlank(context.getProperty("log.elk.destination"))){
            throw new LogbackException("jpower.log.elk.destination 配置为空");
        }

        Properties props = System.getProperties();
        context.putProperty("appName",props.getProperty("jpower.name"));
        context.putProperty("version",props.getProperty("jpower.version"));

        started = true;
    }

    @Override
    public void stop() {
        started = false;
    }

    @Override
    public boolean isStarted() {
        return started;
    }
}
