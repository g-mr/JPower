package com.wlcb.jpower.utils;

import cn.hutool.core.util.ClassUtil;
import com.alibaba.fastjson.JSON;
import com.wlcb.jpower.annotation.JpowerDelayTask;
import com.wlcb.jpower.task.DelayTask;
import com.wlcb.jpower.task.entity.TaskDelay;
import com.wlcb.jpower.enums.TaskStatusEnum;
import com.wlcb.jpower.enums.TaskTypeEnum;
import com.wlcb.jpower.task.jdbc.TaskDelayJdbc;
import com.wlcb.jpower.module.common.support.EnvBeanUtil;
import com.wlcb.jpower.module.common.utils.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.time.DateTimeException;
import java.util.*;
import java.util.concurrent.DelayQueue;

/**
 * @author mr.g
 * @date 2023/6/27 11:27 PM
 */
@Slf4j
@RequiredArgsConstructor
public class DelayTaskUtil {

    private final TaskDelayJdbc taskDelayJdbc;
    private final DelayQueue<DelayTask> delayQueue;


    private static class Singleton{
        private static final DelayTaskUtil DELAY_TASK = new DelayTaskUtil(SpringUtil.getBean(TaskDelayJdbc.class), SpringUtil.getBean(DelayQueue.class));

    }

    public static DelayTaskUtil getInstance(){
        return Singleton.DELAY_TASK;
    }

    /**
     * 新增一个延时任务
     *
     * @author mr.g
     * @param clz 执行类
     * @param params 请求参数
     * @param taskTime 执行时间
     **/
    public void add(Class<?> clz, Map<String,Object> params, Date taskTime){
        if (DateUtil.compare(taskTime, DateUtil.date()) <= 0){
            throw new DateTimeException("执行时间小于当前时间,无法执行...");
        }

        Method mt = getMethod(clz, params);

        //拼装数据
        String methodName = mt.getName();
        JpowerDelayTask delayTask = AnnotationUtil.getAnnotation(mt, JpowerDelayTask.class);

        TaskDelay taskTimeBean = new TaskDelay();
        taskTimeBean.setId(Fc.randomSnowFlakeId());
        taskTimeBean.setName(delayTask.name());
        taskTimeBean.setRetry(delayTask.retry());
        taskTimeBean.setAppName(EnvBeanUtil.getString("spring.application.name"));
        taskTimeBean.setType(TaskTypeEnum.DELAY.getCode());
        taskTimeBean.setTaskTime(taskTime);
        taskTimeBean.setClassPath(ClassUtil.getClassName(clz, false));
        taskTimeBean.setMethodName(methodName);
        taskTimeBean.setMethodParams(JSON.toJSONString(params));
        taskTimeBean.setCreateTime(DateUtil.date());
        taskTimeBean.setUpdateTime(DateUtil.date());
        taskTimeBean.setIsRun(Boolean.FALSE);
        try {
            String userId = ClassUtil.invoke("com.wlcb.jpower.module.dbs.config.LoginUserContext#getUserId",false);
            taskTimeBean.setCreateUser(userId);
            taskTimeBean.setUpdateUser(userId);
        }catch (Exception e){
            log.warn("未找到当前登录用户,不设置...");
        }
        taskTimeBean.setStatus(TaskStatusEnum.EXECUTED.getCode());
        taskTimeBean.setIsDeleted(Boolean.FALSE);

        // 判断这个任务执行时间是否小于当前已经入列任务,如果小于则立即加入当前任务队列
        DelayTask dt = delayQueue.stream().max(Comparator.comparing(DelayTask::getTaskTime)).orElse(new DelayTask());
        if (Fc.notNull(dt) && DateUtil.compare(taskTime, dt.getTaskTime()) <= 0){
            delayQueue.add(BeanUtil.copyProperties(taskTimeBean, DelayTask.class));
        }
        //保存数据库
        taskDelayJdbc.save(taskTimeBean);
    }

    private static Method getMethod(Class<?> clz, Map<String, Object> params) {
        Set<Method> methods = AnnotationUtil.findAnnotatedMethods(clz, JpowerDelayTask.class);
        if (Fc.isEmpty(methods)){
            throw new RuntimeException(ClassUtil.getClassName(clz, false) + "类中不存在可执行的方法...");
        }
        for (Method method : methods) {
            if (method.getParameterCount() == params.size()) {
                if (params.size() == 0) {
                    return method;
                }else {
                    Parameter[] parameters = method.getParameters();
                    if (Arrays.stream(parameters).allMatch(parameter -> params.containsKey(parameter.getName()))){
                        return method;
                    }
                }
            }
        }

        throw new RuntimeException(ClassUtil.getClassName(clz, false) + "类中不存在符合执行的方法...");
    }

    /**
     * 新增一个延时任务
     *
     * @author mr.g
     * @param clz 执行类
     * @param taskTime 执行时间
     **/
    public void add(Class<?> clz, Date taskTime){
        add(clz, MapUtil.empty(), taskTime);
    }

}
