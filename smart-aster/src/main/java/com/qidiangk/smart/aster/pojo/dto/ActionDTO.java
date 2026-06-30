package com.qidiangk.smart.aster.pojo.dto;

import cn.hutool.core.date.DateUtil;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class ActionDTO implements Serializable {

    /**
     * 呼叫id
     */
    private String callId;
    /**
     * 录音内容
     */
    private String content;
    /**
     * 0:用户说的话 1:客服说的话
     */
    private Integer type;
    /**
     * 录音文件base64
     */
    private String recordBase64;

    @Builder.Default
    private Long time = DateUtil.current();

}
