package com.qidiangk.smart.aster.dbs.entity.ivr;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.common.validated.InEnum;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.DictTypeConstants;
import com.qidiangk.smart.aster.constants.OutCallStatusEnum;

import java.time.LocalDateTime;

/**
 * @author mr.g
 */
@Data
@Table(value = "ivr_out_task_call")
@EqualsAndHashCode(callSuper = true)
public class OutTaskCallDO extends TenantEntity {

    /**
     * 记录唯一ID。
     */
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
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
    @Column(value = "params")
    private String callParams;

    /**
     * 实际拨打时间（成功发起呼叫的时间）。
     */
    @Schema(description = "实际拨打时间")
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

}
