package com.qidiangk.smart.aster.pojo.vo.ivr;

import lombok.Builder;
import lombok.Data;
import com.qidiangk.smart.aster.constants.CallHangStateEnum;

import java.io.Serializable;

/**
 * 外呼结果
 *
 * @author mr.g
 */
@Data
@Builder
public class CallVO implements Serializable {

    /**
     * 通话链路ID
     */
    private String linkedId;
    /**
     * 通话结果
     */
    private CallHangStateEnum callResult;

}
