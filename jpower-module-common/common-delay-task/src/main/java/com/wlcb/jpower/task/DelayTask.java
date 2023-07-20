package com.wlcb.jpower.task;

import com.wlcb.jpower.module.common.utils.DateUtil;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * @author mr.g
 * @date 2023/6/26 11:32 PM
 */
@Data
@RequiredArgsConstructor
public class DelayTask implements Delayed {

//    /**
//     * 执行类路径
//     **/
//    private String classPath;
//    /**
//     * 执行方法
//     **/
//    private String methodName;
//    /**
//     * 执行参数
//     **/
//    private String methodParams;
    /**
     * 执行时间
     **/
    private Date taskTime;

    /**
     * 是否已执行
     **/
    private Boolean isRun;
//    /**
//     * 失败重试次数
//     **/
//    private Integer retry;
    /**
     * 任务ID
     **/
    private Long id;

    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(taskTime.getTime() - DateUtil.current(), TimeUnit.MILLISECONDS);
    }

    @Override
    public int compareTo(@NonNull Delayed delayed) {
        return DateUtil.compare(taskTime, ((DelayTask) delayed).getTaskTime());
    }
}
