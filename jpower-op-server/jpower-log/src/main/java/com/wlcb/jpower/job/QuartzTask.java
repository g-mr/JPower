package com.wlcb.jpower.job;

import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author mr.g
 * @date 2021-04-01 17:14
 */
@Slf4j
public class QuartzTask implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("任务开始执行了");
        try {
            executeTask();
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        log.info("任务执行结束了");
    }

    private void executeTask() throws SchedulerException {
    }
}