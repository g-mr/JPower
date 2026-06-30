package com.qidiangk.smart.aster.pojo.vo.line;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
public class LineQueryVO implements Serializable {

    /**
     * 线路号码
     */
    @Schema(description = "线路号码")
    private String callerid;

    /**
     * 是否主动注册
     */
    @Schema(description = "是否主动注册")
    private Boolean isRegister;

    /**
     * 呼入请求是否要求认证
     */
    @Schema(description = "呼入请求是否要求认证")
    private Boolean isAuth;

    /**
     * 连接IP
     */
    @Schema(description = "连接IP")
    private String ip;

}
