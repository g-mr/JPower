package com.qidiangk.smart.aster.dbs.entity.cdr;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.CallOutTypeEnum;
import com.qidiangk.smart.aster.constants.CallStateEnum;
import com.qidiangk.smart.aster.constants.CallTypeEnum;

import java.util.Date;

/**
 * @author mr.g
 */
@Data
@Table(value = "call_info")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
public class CallInfoDO extends TenantEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "通话记录ID")
    private Long id;

    @Schema(description = "呼叫类型 （1：呼入 2：呼出 3：内部通话）")
    @NotNull(message = "呼叫类型 不能为空")
    @Dict(name = "CALL_TYPE")
    private CallTypeEnum type;

    @Schema(description = "用户手机号、内部通话的主叫")
    private String phone;

    @Schema(description = "呼出类型 （10：机器人呼出 21：分机直拨呼出 22：网页拨号呼出）")
    private CallOutTypeEnum outType;

    @Schema(description = "当分机呼出时分机号、呼入分机时候的分机号、内部通话的被叫、转人工时的最后一个坐席")
    private String extension;

    @Schema(description = "路由IVRID")
    private Long ivrId;

    @Schema(description = "是否转人工")
    private Boolean isTransfer;

    @Schema(description = "通道ID")
    private String linkedId;

    @Schema(description = "中继线路")
    private String channelLine;

    @Schema(description = "中继线路通道名称")
    private String channelName;

    @Schema(description = "运行的呼叫计划名称")
    private String context;

    @Schema(description = "当前状态")
    private CallStateEnum state;

    @Schema(description = "挂断状态")
    private CallHangStateEnum hangupState;

    @Schema(description = "挂断原因")
    private String hangupCause;

    @Schema(description = "主动挂断方 user=用户 robot=机器人  分机挂断存分机号 机器人转人工情况存最后一个挂断的分机号")
    private String hangupUser;

    @Schema(description = "拨打时间")
    private Date callTime;

    @Schema(description = "响铃时间")
    private Date ringTime;

    @Schema(description = "接听时间")
    private Date answerTime;

    @Schema(description = "挂断时间")
    private Date hangupTime;

    @Schema(description = "录音文件")
    private String filePath;
}
