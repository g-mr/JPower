package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 搜索条件
 *
 * @author mr.g
 */
@Data
public class CallInfoQueryVO implements Serializable {

    @Schema(description = "呼叫类型")
    private Integer type;

    @Schema(description = "最后坐席")
    private String extension;

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "中继线路")
    private String channelLine;

    @Schema(description = "状态")
    private Integer state;

    @Schema(description = "主动挂断方 user=用户 robot=机器人  分机挂断存分机号 机器人转人工情况存最后一个挂断的分机号")
    private String hangupUser;

    @Schema(description = "通话时长最小")
    private Integer minCallDuration;

    @Schema(description = "通话时长最大")
    private Integer maxCallDuration;

    @Schema(description = "是否只查AI", hidden = true)
    private Boolean ai = false;
}
