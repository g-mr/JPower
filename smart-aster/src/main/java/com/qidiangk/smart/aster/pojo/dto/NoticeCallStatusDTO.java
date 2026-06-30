package com.qidiangk.smart.aster.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;
import com.qidiangk.smart.aster.constants.NoticeStatusEnum;

import java.io.Serializable;
import java.util.Date;

/**
 * 通话状态
 */
@Data
@Accessors(chain = true)
public class NoticeCallStatusDTO implements Serializable {

    /**
     * 	通话唯一标识
     */
    private String callId;
    /**
     * 时间
     */
    private Date startTime;
    /**
     * 状态
     */
    private NoticeStatusEnum status;
    /**
     * 转接信息
     */
    private Transfer transfer;
    /**
     * 呼出时的分机号
     */
//    private String extNum;
    /**
     * 分机挂断
     */
    private Boolean extHungFlag;
    /**
     * 挂断原因
     */
    private CallHangStateEnum byeCause;

    @Data
    @Accessors(chain = true)
    public static class Transfer implements Serializable {
        /**
         * 	转客服状态
         */
        private NoticeStatusEnum status;
        /**
         * 分机号
         */
        private String extNum;
        /**
         * 是否分机挂断
         */
        private Boolean extHungFlag;
        /**
         * 挂断原因
         */
        private CallHangStateEnum byeCause;
    }

}
