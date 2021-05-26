package com.wlcb.jpower.dbs.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 18:59
 */
@Data
public class TbLogError extends TbLogBase {

    private static final long serialVersionUID = 8605157367385279481L;

    @ApiModelProperty("错误信息")
    private String error;
    @ApiModelProperty("报错行号")
    private Integer lineNumber;
    @ApiModelProperty("异常名称")
    private String exceptionName;
    @ApiModelProperty("异常message")
    private String message;
}
