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
import com.wlcb.jpower.module.common.deploy.props.JpowerProperties;
import com.wlcb.jpower.module.common.utils.Fc;
import com.wlcb.jpower.module.common.utils.SpringUtil;
import com.wlcb.jpower.module.common.utils.constants.AppConstant;

import java.util.stream.Collectors;

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

        LogProperties log = SpringUtil.getBean(LogProperties.class);
        JpowerProperties jpower = SpringUtil.getBean(JpowerProperties.class);

        if (log.getMode().contains(LogProperties.LogGenre.elk) && Fc.isBlank(log.getElk().getDestination())){
            throw new LogbackException("jpower.log.elk.destination配置为空");
        }

        context.putProperty("genre",log.getMode().stream().map(Enum::name).distinct().collect(Collectors.joining(",")));
        context.putObject("log",log);
        context.putProperty("level", Fc.equalsValue(jpower.getEnv(), AppConstant.DEV_CODE)?"DEBUG":"INFO");

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
