package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.aster.constants.CallOutTypeEnum;
import com.qidiangk.smart.aster.constants.CallTypeEnum;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallTransferInfoDO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CallInfoAttendVO extends CallTransferInfoDO {

    @Schema(description = "呼叫类型 （1：呼入 2：呼出 3：内部通话）")
    @NotNull(message = "呼叫类型 不能为空")
    private CallTypeEnum type;

    @Schema(description = "呼出类型 （10：机器人呼出 21：分机直拨呼出 22：网页拨号呼出）")
    private CallOutTypeEnum outType;

    @Schema(description = "用户手机号、内部通话的主叫")
    private String phone;

    @Schema(description = "中继线路")
    private String channelLine;

    @Schema(description = "线路号码")
    private String channelCallId;
}
