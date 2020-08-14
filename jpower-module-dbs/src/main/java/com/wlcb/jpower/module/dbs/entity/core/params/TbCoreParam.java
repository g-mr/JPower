package com.wlcb.jpower.module.dbs.entity.core.params;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreParam
 * @Description TODO 系统参数
 * @Author 郭丁志
 * @Date 2020-05-06 16:04
 * @Version 1.0
 */
@Data
public class TbCoreParam extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 3631941874174942414L;

    @ApiModelProperty("参数编码")
    private String code;
    @ApiModelProperty("参数名称")
    private String name;
    @ApiModelProperty("参数值")
    private String value;
    @ApiModelProperty("是否支持立即生效 字典YN01")
    @Dict(name = "YN01", attributes = "isEffectStr")
    private Integer isEffect;
    @ApiModelProperty("备注")
    private String note;

    @ApiModelProperty("是否支持立即生效")
    @TableField(exist = false)
    private String isEffectStr;
}
