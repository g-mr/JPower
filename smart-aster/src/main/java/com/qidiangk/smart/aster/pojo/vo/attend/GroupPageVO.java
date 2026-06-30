package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.qidiangk.smart.aster.constants.QueueStrategyEnum;
import top.jpower.core.dbs.dictbind.annotation.Dict;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
public class GroupPageVO implements Serializable {

    /**
     * 队列名称
     */
    @Schema(description = "队列名称,不能有中文")
    @NotBlank(message = "队列名称不可为空")
    private String name;
    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    @NotBlank(message = "队列显示名称不可为空")
    private String showName;
    /**
     * 等待音乐，当呼叫在队列中等待时播放的音乐
     * TODO 这个回头设置成可以选择的，关联到提示音管理
     */
    @Schema(description = "等待音乐")
    private String musiconhold;
    /**
     * 分配策略，呼叫分配给成员的策略
     */
    @Schema(description = "分配策略")
    @NotNull(message = "分配策略不可为空")
    @Dict(name = "ATTEND_STRATEGY")
    private QueueStrategyEnum strategy;
    /**
     * 是否让多个等待呼叫同时预测坐席空闲时间并提前开始振铃
     */
    @Schema(description = "是否让多个等待呼叫同时预测坐席空闲时间并提前开始振铃")
    private Boolean autofill;
    /**
     * 超时时间，队列中每个成员振铃的时间（秒）
     */
    @Schema(description = "超时时间，队列中每个成员振铃的时间（秒）")
    @NotNull(message = "超时时间不可为空")
    private Integer timeout;
    /**
     * 当呼叫被转移到另一个坐席时，是否重置超时计时器。
     */
    @Schema(description = "当呼叫被转移到另一个坐席时，是否重置超时计时器。")
    private Boolean timeoutrestart;
    /**
     * 重试次数，当没有成员应答时，重新尝试的次数
     */
    @Schema(description = "重试次数，当没有成员应答时，重新尝试的次数")
    private Integer retry;
    /**
     * 	如果坐席分机正忙（但未接听），是否仍然向其振铃。通常设为 no。
     */
    @Schema(description = "如果坐席分机正忙（但未接听），是否仍然向其振铃。")
    private Boolean ringinuse;
    /**
     * 话后处理时间，成员处理完一个呼叫后的清理时间（秒）
     */
    @Schema(description = "话后处理时间，成员处理完一个呼叫后的清理时间（秒）")
    private Integer wrapuptime;

    @Schema(description = "成员数量")
    private Integer memberNum;

    private Map<String, Object> params = new HashMap<>();

}
