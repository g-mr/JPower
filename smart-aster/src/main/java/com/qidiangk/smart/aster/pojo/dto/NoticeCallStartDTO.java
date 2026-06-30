package com.qidiangk.smart.aster.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import com.qidiangk.smart.aster.constants.CallTypeEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 通话开始信息
 */
@Data
@Accessors(chain = true)
public class NoticeCallStartDTO implements Serializable {

    /**
     * 	主叫号码
     */
    private String from;
    /**
     * 中继号码
     */
    private String trunkName;
    /**
     * 时间
     */
    private Date startTime;
    /**
     * 	呼叫方向
     * 	1=呼入
     * 	2=呼出
     * 	3=内部通话
     */
    private CallTypeEnum direction;
    /**
     * 	通话唯一标识
     */
    private String callId;
    /**
     * 被叫号码
     */
    private String to;
    /**
     * 是否ai呼出
     */
    private Boolean aiOutbound;

}
