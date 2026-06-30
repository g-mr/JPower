package com.qidiangk.smart.aster.dbs.entity.cdr;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.CallStateEnum;

import java.util.Date;

@Data
@Table(value = "call_transfer_info")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CallTransferInfoDO extends TenantEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "通话转接记录ID")
    private Long id;

    @Schema(description = "通话记录ID")
    private Long callInfoId;

    @Schema(description = "转接分机号")
    private String extension;

    @Schema(description = "队列名称")
    private String queue;

    @Schema(description = "转接线路ID")
    private String uniqueId;

    @Schema(description = "当前状态")
    private CallStateEnum state;

    @Schema(description = "挂断状态")
    private CallHangStateEnum hangupState;

    @Schema(description = "挂断原因")
    private String hangupCause;

    @Schema(description = "是否分机主动挂断")
    private Boolean hangupExt;

    @Schema(description = "准备转接时间")
    private Date readyTime;

    @Schema(description = "响铃时间")
    private Date ringTime;

    @Schema(description = "接听时间")
    private Date answerTime;

    @Schema(description = "挂断时间")
    private Date hangupTime;

    @Schema(description = "录音文件")
    private String filePath;
}
