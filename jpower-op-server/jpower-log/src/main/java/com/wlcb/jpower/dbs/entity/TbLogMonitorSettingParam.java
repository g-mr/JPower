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
public class TbLogMonitorSettingParam extends BaseEntity {

    @ApiModelProperty(value = "主键",hidden = true)
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;


    @ApiModelProperty(value = "设置ID",hidden = true)
    private String settingId;
    @ApiModelProperty("参数类型 字典:PARAM_TYPE")
    private String type;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("参数值")
    private String value;

}
