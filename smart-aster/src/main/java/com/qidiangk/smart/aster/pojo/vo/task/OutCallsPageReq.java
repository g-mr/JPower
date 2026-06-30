package com.qidiangk.smart.aster.pojo.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class OutCallsPageReq implements Serializable {

    @Schema(description = "任务名称")
    private String name;

    @Schema(description = "启动方式：1-手动启动，2-定时启动")
    private Integer startWay;

    @Schema(description = "外呼线路ID")
    private String lineId;

    @Schema(description = "任务状态：0-未开始，1-已开始，2-已结束")
    private Integer status;

    @Schema(description = "路由ID")
    private Long routeId;

}
