package com.qidiangk.smart.aster.pojo.vo.cdr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.aster.dbs.entity.cdr.CallInfoDO;

@Data
@EqualsAndHashCode(callSuper = true)
public class CallInfoVO extends CallInfoDO {

    @Schema(description = "IVR名称")
    private String ivrName;

    @Schema(description = "线路号码")
    private String channelCallId;

}
