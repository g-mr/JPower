package com.qidiangk.smart.aster.pojo.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.aster.constants.CallOutTypeEnum;
import com.qidiangk.smart.aster.constants.CallTypeEnum;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class PieChatVO implements Serializable {

    @Schema(description = "数量")
    private Integer value;

    @Schema(description = "类型")
    private String name;

    @Schema(description = "呼叫类型（1：呼入 2：呼出 3：内部通话）")
    private CallTypeEnum type;
    @Schema(description = "呼出类型 （10：机器人呼出 21：分机直拨呼出 22：网页拨号呼出）")
    private CallOutTypeEnum outType;
}
