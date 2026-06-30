package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class TransferQueryVO implements Serializable {

    @Schema(description = "通话记录ID")
    @NotNull(message = "通话记录ID不能为空")
    private Long callInfoId;

    @Schema(description = "转接分机号")
    private String extension;

    @Schema(description = "队列名称")
    private String queue;

    @Schema(description = "当前状态")
    private Integer state;

    @Schema(description = "是否分机主动挂断")
    private Boolean hangupExt;

    @Schema(description = "通话时长最小")
    private Integer minCallDuration;

    @Schema(description = "通话时长最大")
    private Integer maxCallDuration;

}
