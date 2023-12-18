package top.jpower.jpower.task.entity;


import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;
import top.jpower.jpower.annotation.JEntity;
import top.jpower.jpower.annotation.JId;

import java.io.Serializable;
import java.util.Date;

/**
 * @author mr.g
 * @date 2023/6/27 9:34 PM
 */
@Data
@Accessors(chain = true)
@JEntity("延时任务表")
public class TaskDelay implements Serializable {
    private static final long serialVersionUID = 4467472629255752791L;

    /**
     * 主键
     **/
    @JId
    private Long id;

    /**
     * 任务名称
     **/
    private String name;

    /**
     * 执行服务名称
     **/
    private String appName;

    /**
     * 任务类型 <br/>
     * e.g: 1延时任务
     **/
    private Integer type;

    /**
     * 执行时间
     **/
    private Date taskTime;

    /**
     * 执行类路径
     **/
    private String classPath;

    /**
     * 执行方法名
     **/
    private String methodName;

    /**
     * 执行参数
     **/
    private String methodParams;

    /**
     * 失败重试次数
     **/
    private Integer retry;

    /**
     * 是否在执行
     **/
    private Boolean isRun;

    /**
     * 创建人
     **/
    private String createUser;

    /**
     * 创建时间
     **/
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    private Date createTime;

    /**
     * 修改人
     **/
    private String updateUser;

    /**
     * 修改时间
     **/
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    private Date updateTime;

    /**
     * 执行状态 <br/>
     * e.g：0执行失败 1待执行 10执行完成
     **/
    private Integer status;

    /**
     * 是否删除
     **/
    @JsonIgnore
    @JSONField(serialize = false)
    private Boolean isDeleted;
}
