package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author mr.g
 * @Date 2021/5/1 0001 18:59
 */
@Data
public class TbLogError extends BaseEntity {

    @ApiModelProperty("服务名称")
    private String serviceName;
    @ApiModelProperty("服务器 ip")
    private String serverIp;
    @ApiModelProperty("服务器名")
    private String serverHost;
    @ApiModelProperty("环境")
    private String env;
    @ApiModelProperty("操作IP地址")
    private String remoteIp;
    @ApiModelProperty("请求url")
    private String url;
    @ApiModelProperty("操作方式")
    private String method;
    @ApiModelProperty("方法类")
    private String methodClass;
    @ApiModelProperty("方法名")
    private String methodName;
    @ApiModelProperty("请求参数")
    private String param;
    @ApiModelProperty("操作人员")
    private String operName;
    @ApiModelProperty("操作人员类型，是系统用户还是业务用户 0系统1业务2白名单")
    private Integer operUserType;
    @ApiModelProperty("操作客户端")
    private String clientCode;

    @ApiModelProperty("错误信息")
    private String error;
    @ApiModelProperty("报错行号")
    private Integer lineNumber;
    @ApiModelProperty("异常名称")
    private String exceptionName;
    @ApiModelProperty("异常message")
    private String message;
}
