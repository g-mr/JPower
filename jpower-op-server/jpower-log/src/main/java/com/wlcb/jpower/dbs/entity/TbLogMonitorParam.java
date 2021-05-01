package com.wlcb.jpower.dbs.entity;

import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * @Author mr.g
 * @Date 2021/4/19 0019 18:46
 */
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Data
public class TbLogMonitorParam extends BaseEntity {

    private static final long serialVersionUID = 5487153187619562214L;


    @ApiModelProperty(value = "服务名称",hidden = true)
    private String server;
    @ApiModelProperty(value = "监控地址",hidden = true)
    private String path;
    @ApiModelProperty(value = "请求方式",hidden = true)
    private String method;

    @ApiModelProperty("参数类型 字典:PARAM_TYPE")
    private String type;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("参数值")
    private String value;

}
