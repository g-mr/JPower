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
public class TbLogMonitorSetting extends BaseEntity {

    @ApiModelProperty("服务名称")
    private String name;
    @ApiModelProperty("监控地址")
    private String path;
    @ApiModelProperty("所属分组")
    private String tag;
    @ApiModelProperty("请求方式")
    private String method;
    @ApiModelProperty("是否监控 字典YN_MONITOR")
    private Integer isMonitor;
    @ApiModelProperty("respose正确status,多个逗号分割")
    private String code;
    @ApiModelProperty("js代码")
    private String execJs;

    @ApiModelProperty("是否生效 0不生效 1生效")
    private Integer status;

//    @ApiModelProperty(hidden = true)
//    @JSONField(serialize = false)
//    @TableField(exist = false)
//    @JsonIgnore
//    private List<Map<String,Object>> settingParams;

}
