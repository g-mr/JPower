package com.qidiangk.smart.aster.pojo.vo.task;

import cn.hutool.core.date.DatePattern;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.common.validated.InEnum;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.DictTypeConstants;
import com.qidiangk.smart.aster.constants.OutCallStatusEnum;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.g
 */
@Data
public class PhoneListVO implements Serializable {

    private Map<String, Object> params = new HashMap<>();

    /**
     * 记录唯一ID。
     */
    @Schema(description = "记录唯一ID")
    private Long id;

    /**
     * 任务ID。
     */
    @Schema(description = "任务ID")
    private Long taskId;

    /**
     * 被叫手机号码。
     */
    @Schema(description = "被叫手机号码")
    private String phone;

    /**
     * 外呼时传递的初始参数，JSON 格式，用于机器人场景。
     */
    @Schema(description = "外呼时传递的初始参数，JSON 格式")
    private String callParams;

    /**
     * 实际拨打时间（成功发起呼叫的时间）。
     */
    @Schema(description = "实际拨打时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING,timezone = "GMT+8", pattern = DatePattern.NORM_DATETIME_PATTERN,locale = "zh_CN")
    @JSONField(format= DatePattern.NORM_DATETIME_PATTERN)
    private LocalDateTime callTime;

    /**
     * 拨打状态：0-未拨打，1-已拨打，2-拨打中。
     */
    @Schema(description = "拨打状态")
    @Dict(name = DictTypeConstants.OUT_CALLS_STATUS)
    @InEnum(OutCallStatusEnum.class)
    private Integer status;

    /**
     * 拨打结果
     */
    @Schema(description = "拨打结果")
    @Dict(name = DictTypeConstants.CALL_STATE)
    private CallHangStateEnum callResult;

    /**
     * 已拨打次数，用于重呼计数。
     */
    @Schema(description = "已拨打次数")
    private Integer count;

    /**
     * 通话链路ID，对应 Asterisk 中的 linkedId，用于关联 CDR 和日志。
     */
    @Schema(description = "通话链路ID")
    private String linkedId;

    @Schema(description = "录音文件")
    private String path;

}
