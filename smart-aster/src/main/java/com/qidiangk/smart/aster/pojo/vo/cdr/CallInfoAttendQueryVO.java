package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class CallInfoAttendQueryVO implements Serializable {

    @Schema(description = "呼叫类型")
    private Integer type;

    @Schema(description = "坐席")
    @NotBlank(message = "坐席不可为空")
    private String extension;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "中继线路")
    private String channelLine;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "坐席组")
    private String queue;

    @Schema(description = "通话时长最小")
    private Integer minCallDuration;

    @Schema(description = "通话时长最大")
    private Integer maxCallDuration;

    @Schema(description = "是否查询呼损记录", hidden = true)
    private Boolean isCallLoss = false;

}
