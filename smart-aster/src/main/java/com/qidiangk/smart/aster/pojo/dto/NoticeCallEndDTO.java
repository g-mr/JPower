package com.qidiangk.smart.aster.pojo.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * 通话结束通知
 */
@Data
@Accessors(chain = true)
public class NoticeCallEndDTO implements Serializable {

    /**
     * 	通话唯一标识
     */
    private String callId;
    /**
     * 主叫号码
     */
    private String from;
    /**
     * 被叫
     */
    private String to;
    /**
     * 通话开始时间戳
     */
    private Date startTime;
    /**
     * 通话结束时间戳
     */
    private Date endTime;
    /**
     * 通话时长
     */
    private Long duration;
    /**
     * 录音文件访问URL
     */
    private String url;

}
