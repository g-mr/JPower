package com.qidiangk.smart.aster.pojo.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class OverviewVO implements Serializable {

    @Schema(description = "总坐席数")
    private Integer totalAgents;
    @Schema(description = "空闲坐席")
    private Integer idleAgents;
    @Schema(description = "忙碌坐席")
    private Integer busyAgents;
    @Schema(description = "在线坐席")
    private Integer onlineAgents;
    @Schema(description = "在线坐席与昨日相比")
    private Integer agentChange;
    @Schema(description = "AI处理")
    private Integer aiHandled;
    @Schema(description = "平均时长")
    private Double avgDuration;
    @Schema(description = "AI通话占比")
    private Double satisfaction;
    @Schema(description = "总通话数")
    private Integer totalCalls;
    @Schema(description = "总通话数与昨日相比")
    private Integer callChange;
    @Schema(description = "AI通话")
    private Integer aiCalls;
    @Schema(description = "AI通话与昨日相比")
    private Integer aiChange;
    @Schema(description = "接通率")
    private Double connectRate;
    @Schema(description = "接通率与昨日相比")
    private Double rateChange;

}
