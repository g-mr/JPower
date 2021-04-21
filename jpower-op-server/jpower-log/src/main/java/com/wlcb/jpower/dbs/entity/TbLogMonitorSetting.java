package com.wlcb.jpower.dbs.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
public class TbLogMonitorSetting extends BaseEntity {

    private static final long serialVersionUID = -884603554775809089L;

    @ApiModelProperty("服务名称")
    private String server;
    @ApiModelProperty("监控地址")
    private String path;
    @ApiModelProperty("所属分组")
    private String tag;
    @ApiModelProperty("请求方式")
    private String method;
    @ApiModelProperty("是否监控 字典YN01")
    private Integer isMonitor;
    @ApiModelProperty("respose正确status,多个逗号分割")
    private String code;
    @ApiModelProperty("js代码")
    private String execJs;

}
