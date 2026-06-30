package com.qidiangk.smart.aster.pojo.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.aster.constants.TaskStartWayEnum;
import com.qidiangk.smart.aster.constants.TaskStatusEnum;
import top.jpower.core.dbs.dictbind.annotation.Dict;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.g
 */
@Data
public class OutCallsVO implements Serializable {

    private Map<String, Object> params = new HashMap<>();

    /**
     * 外呼任务唯一ID。
     */
    @Schema(description = "外呼任务唯一ID")
    private Long id;

    /**
     * 外呼任务名称，用于标识任务用途。
     */
    @Schema(description = "外呼任务名称")
    private String name;

    @Schema(description = "外呼线路ID")
    private String lineId;

    /**
     * 任务优先级
     **/
    @Schema(description = "任务优先级")
    private Integer priorities;

    /**
     * 启动方式：1-手动启动，2-定时启动。
     */
    @Schema(description = "启动方式：1-手动启动，2-定时启动")
    @Dict(name = "TASK_START_WAY")
    private TaskStartWayEnum startWay;

    /**
     * 并发机器人数量，即同时外呼的最大通道数，对应 Asterisk 中的最大并发呼叫数。
     */
    @Schema(description = "并发机器人数量")
    private Integer robotNum;

    /**
     * 失败是否重新呼叫（true=是，false=否）。
     */
    @Schema(description = "失败是否重新呼叫")
    private Boolean isReCall;

    /**
     * 是否自动去除重复导入的号码（true=去除，false=保留重复）。
     */
    @Schema(description = "是否自动去除重复导入的号码")
    private Boolean isDuplicates;

    /**
     * 任务状态：1-未开始，2-已开始。
     */
    @Schema(description = "任务状态：0-未开始，1-已开始，2-已结束")
    @Dict(name = "TASK_STATUS")
    private TaskStatusEnum status;

    /**
     * 总呼叫量
     */
    @Schema(description = "总呼叫量")
    private Integer totalCallNum;

    /**
     * 待呼叫量
     */
    @Schema(description = "待呼叫量")
    private Integer waitCallNum;

    /**
     * 路由名称
     */
    @Schema(description = "路由名称")
    private String routeName;

    @Schema(description = "外呼线路")
    private String lineCallerid;

}
