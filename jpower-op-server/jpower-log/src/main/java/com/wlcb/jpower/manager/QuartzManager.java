package com.wlcb.jpower.manager;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.support.CronTrigger;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author mr.g
 * @date 2021-04-01 16:41
 */
@Configuration
public class QuartzManager {

    @Autowired
    private Scheduler scheduler;

    /**
     * 开始执行所有任务
     */
    public void startJob() throws SchedulerException {
        //启动时进行查库将任务添加至job

        List<String> netaskList = new ArrayList<>();
        List<String> zctaskList = new ArrayList<>();

        //添加任务至调度器scheduler
        startJob1(scheduler,netaskList);
        //调度任务开始执行
        //		scheduler.start();
        startJob2(scheduler,zctaskList);
        scheduler.start();
    }

    /*
     * 重启所有任务
     */
    public void restartJob() throws SchedulerException, InterruptedException {
        //不可以用shutdown，也不需要停止，直接清除，然后启动
        //		scheduler.shutdown();
        //		scheduler.pauseAll();
        scheduler.clear();
        this.startJob();
    }

    /**
     * title:
     * mentality:
     * @throws
     * @param scheduler2
     * @param zctaskList
     */
    private void startJob2(Scheduler scheduler2, List<String> zctaskList) throws SchedulerException{
        // TODO Auto-generated method stub


    }

    /**
     * title:计划任务1
     * mentality:
     * @throws
     * @param scheduler2
     * @param netaskList
     */
    private void startJob1(Scheduler scheduler2, List<String> netaskList) throws SchedulerException{

    }


    /**
     * 获取Job信息
     *
     * @param name
     * @param group
     * @return
     * @throws SchedulerException
     */
    public String getJobInfo(String name, String group) throws SchedulerException {
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        return String.format("time:%s,state:%s", cronTrigger.getExpression(),
                scheduler.getTriggerState(triggerKey).name());
    }

    /**
     * 修改某个任务的执行时间
     * (修改的是具体的trigger，不是jobdetail）
     * @param name
     * @param group
     * @param time
     * @return
     * @throws SchedulerException
     */
    public boolean modifyJob(String name, String group, String time) throws SchedulerException {
        Date date = null;
        TriggerKey triggerKey = new TriggerKey(name, group);
        CronTrigger cronTrigger = (CronTrigger) scheduler.getTrigger(triggerKey);
        String oldTime = cronTrigger.getExpression();
        if (!oldTime.equalsIgnoreCase(time)) {
            CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(time);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
                    .withSchedule(cronScheduleBuilder).build();
            date = scheduler.rescheduleJob(triggerKey, trigger);
        }
        return date != null;
    }

    /**
     * 暂停所有任务
     *
     * @throws SchedulerException
     */
    public void pauseAllJob() throws SchedulerException {
        scheduler.pauseAll();
    }

    /**
     * 暂停某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void pauseJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null){
            return;
        }

        scheduler.pauseJob(jobKey);
    }

    /**
     * 恢复所有任务
     *
     * @throws SchedulerException
     */
    public void resumeAllJob() throws SchedulerException {
        scheduler.resumeAll();
    }

    /**
     * 恢复某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void resumeJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null){
            return;
        }
        scheduler.resumeJob(jobKey);
    }

    /**
     * 删除某个任务
     *
     * @param name
     * @param group
     * @throws SchedulerException
     */
    public void deleteJob(String name, String group) throws SchedulerException {
        JobKey jobKey = new JobKey(name, group);
        JobDetail jobDetail = scheduler.getJobDetail(jobKey);
        if (jobDetail == null){
            return;
        }
        scheduler.deleteJob(jobKey);
    }
}

