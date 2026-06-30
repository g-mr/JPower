package com.qidiangk.smart.aster.pojo.vo.cdr;

import com.mybatisflex.annotation.Column;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.CallOutTypeEnum;
import com.qidiangk.smart.aster.constants.CallStateEnum;
import com.qidiangk.smart.aster.constants.CallTypeEnum;
import top.jpower.core.dbs.dictbind.annotation.Dict;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.qidiangk.smart.aster.constants.DictTypeConstants.CALL_STATE;

/**
 * @author mr.g
 */
@Data
@Schema(description = "坐席与电话号码通话记录 VO")
public class CallInfoByAttendPhoneVO implements Serializable {

    @Schema(description = "电话号码")
    private String phone;

    @Schema(description = "拨打日期")
    private Date callTime;

    @Schema(description = "呼叫类型 （1：呼入 2：呼出 3：内部通话）")
    private CallTypeEnum type;

    @Schema(description = "呼出类型 （10：机器人呼出 21：分机直拨呼出 22：网页拨号呼出）")
    private CallOutTypeEnum outType;

    @Schema(description = "线路号码")
    private String channelLine;

    @Schema(description = "响铃时间")
    private Date ringTime;

    @Schema(description = "接听时间")
    private Date answerTime;

    @Schema(description = "挂断时间")
    private Date hangupTime;

    @Schema(description = "当前状态")
    @Dict(name = CALL_STATE)
    private CallStateEnum state;

    @Schema(description = "挂断状态")
    private CallHangStateEnum hangupState;

    @Schema(description = "是否分机挂机")
    private Boolean hangupExt;

    @Schema(description = "录音文件")
    private String filePath;


    /**
     * 扩展参数/字典翻译
     **/
    private Map<String, Object> params = new HashMap<>();

}
