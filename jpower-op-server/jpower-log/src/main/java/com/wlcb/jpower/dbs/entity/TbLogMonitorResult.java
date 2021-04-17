package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.base.annotation.Excel;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @author mr.g
 * @date 2021-04-07 15:53
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class TbLogMonitorResult extends BaseEntity {

    private static final long serialVersionUID = 5898028806232318030L;

    @ApiModelProperty("服务名称")
    @Excel(name = "服务名称")
    private String name;
    @ApiModelProperty("监控地址")
    @Excel(name = "监控地址")
    private String path;
    @ApiModelProperty("所属分组")
    @Excel(name = "所属分组")
    private String tags;
    @ApiModelProperty("接口地址")
    @Excel(name = "测试地址")
    private String url;
    @ApiModelProperty("请求方式")
    @Excel(name = "请求方式")
    private String method;
    @ApiModelProperty("请求异常")
    @Excel(name = "请求异常")
    private String error;
    @ApiModelProperty("响应数据")
    @Excel(name = "响应数据")
    private String respose;
    @ApiModelProperty("响应编码")
    @Excel(name = "响应编码")
    private Integer resposeCode;
    @ApiModelProperty("接口返回数据")
    @Excel(name = "接口返回数据")
    private String restfulResponse;
    @ApiModelProperty("header参数")
    @Excel(name = "header参数")
    private String header;
    @ApiModelProperty("body参数")
    @Excel(name = "请求参数")
    private String body;
}
