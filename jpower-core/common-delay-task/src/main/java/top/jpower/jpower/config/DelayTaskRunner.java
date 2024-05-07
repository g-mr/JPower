package top.jpower.jpower.config;

import cn.hutool.core.thread.ThreadUtil;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import top.jpower.core.deploy.property.JpowerProperties;
import top.jpower.core.util.utils.*;
import top.jpower.jpower.enums.TaskStatusEnum;
import top.jpower.jpower.enums.TaskTypeEnum;
import top.jpower.jpower.task.DelayTask;
import top.jpower.jpower.task.entity.TaskDelay;
import top.jpower.jpower.task.jdbc.TaskDelayJdbc;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.DelayQueue;

/**
 * @author mr.g
 * @date 2023/6/26 11:55 PM
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DelayTaskRunner implements InitializingBean {

    private final DelayQueue<DelayTask> delayQueue;
    private final TaskDelayJdbc taskDelayJdbc;
    private final JpowerProperties jpowerProperties;

    @Override
    public void afterPropertiesSet() {
        ThreadUtil.execute(() -> {
            while(true) {
                try {
                    DelayTask task = getData();
                    //在启动线程的时候要去更新数据库,把当前任务标识为正在执行,防止其他集群重复执行
                    if (Fc.notNull(task)){
                        if (Fc.notNull(task) && !task.getIsRun()){
                            //执行之前要查找任务任务是否存在,如果不存在说明任务已经删除不需要再执行
                            TaskDelay taskDelay = taskDelayJdbc.getByIdAndUpdateIsRun(task.getId());
                            if (Fc.notNull(taskDelay) && !taskDelay.getIsRun() && Fc.equalsValue(taskDelay.getStatus(), TaskStatusEnum.EXECUTED.getCode())){
                                taskRun(taskDelay);
                            }
                        }
                    }

                } catch (Exception e) {
                    log.warn("延时任务报错={},5秒后继续执行...", ExceptionUtil.getStackTraceAsString(e));
                    ThreadUtil.sleep(1000*5);
                }
            }
        });
    }

    /**
     * 开始执行任务
     * @author mr.g
     * @param taskDelay 任务
     **/
    private void taskRun(TaskDelay taskDelay) {
        ThreadUtil.execAsync(() -> {
            try {
                Map<String,Object> paramsMap = JSONObject.parseObject(taskDelay.getMethodParams());
                Object[] params = new Object[paramsMap.size()];
                List<Method> methods = ReflectUtil.getPublicMethods(Class.forName(taskDelay.getClassPath()), method -> {
                    if (Fc.equalsValue(method.getName(), taskDelay.getMethodName()) && method.getParameterCount() == paramsMap.size()){
                        Parameter[] parameters = method.getParameters();

                        for (int i = 0; i < parameters.length; i++) {
                            String name = parameters[i].getName();
                            if (paramsMap.containsKey(name)){
                                params[i] = paramsMap.get(name);
                            } else {
                                return false;
                            }
                        }
                        return true;
                    }
                    return false;
                });

                if (Fc.isNotEmpty(methods)){
                    log.info("[{}]开始执行延时任务,执行类=>{},执行方法=>{},执行参数={}", taskDelay.getId(), taskDelay.getClassPath(), taskDelay.getMethodName(), taskDelay.getMethodParams());

                    Object object = SpringUtil.getBean(Class.forName(taskDelay.getClassPath()));
                    if (Fc.isNull(object)){
                        ReflectUtil.invoke(ReflectUtil.newInstance(taskDelay.getClassPath()), methods.get(0), params);
                    } else {
                        ReflectUtil.invoke(object, methods.get(0), params);
                    }
                    log.info("[{}]延时任务执行完成,执行类=>{},执行方法=>{},执行参数={}", taskDelay.getId(), taskDelay.getClassPath(), taskDelay.getMethodName(), taskDelay.getMethodParams());

                    taskDelayJdbc.updateStatusById(TaskStatusEnum.FINISH.getCode(), taskDelay.getId());
                } else {
                    log.error("[{}]延时任务未找到执行目标,执行类=>{},执行方法=>{},执行参数={}", taskDelay.getId(), taskDelay.getClassPath(), taskDelay.getMethodName(), taskDelay.getMethodParams());
                    throw new RuntimeException("延时任务未找到执行目标");
                }
            } catch (Exception e) {
                //这里要更新数据库把任务标识标识为失败,判断失败重试次数,如果还有次数可让失败任务继续执行
                log.error("[{}]任务执行失败={}", taskDelay.getId(), ExceptionUtil.getStackTraceAsString(e));
                if (taskDelay.getRetry() > 0){
                    log.warn("[{}]任务进行重试,重试还剩{}次", taskDelay.getId(), taskDelay.getRetry());
                    taskDelay.setRetry(taskDelay.getRetry()-1);
                    taskRun(taskDelay);
                } else {
                    taskDelayJdbc.updateStatusById(TaskStatusEnum.FAIL.getCode(), taskDelay.getId());
                }
            }
        }, false);
    }

    /**
     * 去读取任务
     *
     * @return 返回一条任务
     * @author mr.g
     */
    private synchronized DelayTask getData() throws InterruptedException {
        while (delayQueue.size() == 0){
            Long count = taskDelayJdbc.countByServer(jpowerProperties.getApplicationName(), TaskStatusEnum.EXECUTED, TaskTypeEnum.DELAY);
            if (count > 0){
                List<TaskDelay> list = taskDelayJdbc.queryByServer(jpowerProperties.getApplicationName(), TaskStatusEnum.EXECUTED, TaskTypeEnum.DELAY);
                delayQueue.addAll(BeanUtil.copyToList(list, DelayTask.class));
            } else {
                // 如果没有任务的时候,休息一秒再去查
                ThreadUtil.sleep(1000);
            }
        }
        return delayQueue.take();
    }

}
