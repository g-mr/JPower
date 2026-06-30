package com.qidiangk.smart.aster.pojo.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class PhoneQueryVO implements Serializable {

    /**
     * 任务ID。
     */
    @Schema(description = "任务ID")
    @NotNull(message = "任务ID不可为空")
    private Long taskId;

    /**
     * 被叫手机号码。
     */
    @Schema(description = "被叫手机号码")
    private String phone;

    /**
     * 拨打状态：0-未拨打，1-已拨打，2-拨打中。
     */
    @Schema(description = "拨打状态")
    private Integer status;

    /**
     * 拨打结果
     */
    @Schema(description = "拨打结果")
    private Integer callResult;

}
