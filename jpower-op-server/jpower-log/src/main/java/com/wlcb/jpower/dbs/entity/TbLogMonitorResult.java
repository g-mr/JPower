package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author mr.g
 * @date 2021-04-07 15:53
 */
@Data
public class TbLogMonitorResult extends BaseEntity {

    private static final long serialVersionUID = 5898028806232318030L;

    @ApiModelProperty("接口地址")
    private String url;
    @ApiModelProperty("接口方式")
    private String method;
    @ApiModelProperty("请求异常")
    private String error;
    @ApiModelProperty("响应数据")
    private String respose;
    @ApiModelProperty("响应编码")
    private Integer resposeCode;
    @ApiModelProperty("接口返回数据")
    private String restfulResponse;
    @ApiModelProperty("header参数")
    private String header;
    @ApiModelProperty("form参数")
    private String form;
    @ApiModelProperty("body参数")
    private String body;

}
