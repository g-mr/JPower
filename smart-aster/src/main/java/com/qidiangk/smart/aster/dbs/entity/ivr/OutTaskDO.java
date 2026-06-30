package com.qidiangk.smart.aster.dbs.entity.ivr;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.dbs.support.type.IntegerListTypeHandler;
import top.jpower.core.dbs.support.type.StringListTypeHandler;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.TaskStartWayEnum;
import com.qidiangk.smart.aster.constants.TaskStatusEnum;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 外呼任务
 *
 * @author mr.g
 */
@Data
@Table(value = "ivr_out_task")
@EqualsAndHashCode(callSuper = true)
public class OutTaskDO extends TenantEntity {


    /**
     * 外呼任务唯一ID。
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "外呼任务唯一ID")
    @NotNull(message = "外呼任务ID不能为空", groups = {Validation.Update.class})
    private Long id;

    /**
     * 外呼任务名称，用于标识任务用途。
     */
    @Schema(description = "外呼任务名称")
    @NotBlank(message = "外呼任务名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;

    /**
     * 任务优先级
     **/
    @Schema(description = "任务优先级")
    private Integer priorities;

    /**
     * 两次外呼之间的休息间隔（秒），用于控制呼叫速率，避免过载。
     */
    @Schema(description = "两次外呼之间的休息间隔（秒）")
    private Integer restSeconds;

    /**
     * 启动方式：1-手动启动，2-定时启动。
     */
    @Schema(description = "启动方式：1-手动启动，2-定时启动")
    private TaskStartWayEnum startWay;

    /**
     * 定时启动时的开始日期，当 startWay=2 时有效。
     */
    @Schema(description = "定时启动时的开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATE_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATE_PATTERN)
    private LocalDate startDate;

    /**
     * 允许拨打的时间段，多个时间段用逗号分隔，格式 HH:mm-HH:mm，例如 "09:00~12:00,13:00~18:00"。
     */
    @Column(typeHandler = StringListTypeHandler.class)
    @Schema(description = "允许拨打的时间段")
    private List<String> times;

    /**
     * 允许拨打的星期，用数字表示（1=周一，7=周日），多个用逗号分隔，例如 "1,2,3,4,5"。
     */
    @Schema(description = "允许拨打的星期")
    @Column(typeHandler = IntegerListTypeHandler.class)
    private List<Integer> weeks;

    /**
     * 并发机器人数量，即同时外呼的最大通道数，对应 Asterisk 中的最大并发呼叫数。
     */
    @Schema(description = "并发机器人数量")
    private Integer robotNum;

    /**
     * 呼出路由ID，关联路由表，决定呼叫使用的 dialplan 路由策略。
     */
    @Schema(description = "呼出路由ID")
    @NotNull(message = "呼出路由ID不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private Long routeId;

    /**
     * 外呼线路ID，关联线路表，指定使用的 SIP trunk 或 DAHDI 线路。
     */
    @Schema(description = "外呼线路ID")
    @NotNull(message = "外呼线路ID不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String lineId;

    /**
     * 失败是否重新呼叫（true=是，false=否）。
     */
    @Schema(description = "失败是否重新呼叫")
    private Boolean isReCall;

    /**
     * 触发重新呼叫的状态码。
     */
    @Schema(description = "触发重新呼叫的状态码")
    @Column(typeHandler = IntegerListTypeHandler.class)
    private List<Integer> reCallStatus;

    /**
     * 最大重新呼叫次数。
     */
    @Schema(description = "最大重新呼叫次数")
    private Integer reCallNum;

    /**
     * 重新呼叫间隔时间（分钟）。
     */
    @Schema(description = "重新呼叫间隔时间（分钟）")
    private Integer reCallMinutes;

    /**
     * 是否自动去除重复导入的号码（true=去除，false=保留重复）。
     */
    @Schema(description = "是否自动去除重复导入的号码")
    private Boolean isDuplicates;

    /**
     * 任务状态：1-未开始，2-已开始。
     */
    @Schema(description = "任务状态：0-未开始，1-已开始，2-已结束")
    private TaskStatusEnum status;

}
