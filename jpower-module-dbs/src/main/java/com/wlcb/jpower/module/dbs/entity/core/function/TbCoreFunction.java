package com.wlcb.jpower.module.dbs.entity.core.function;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreUser
 * @Description TODO 菜单信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreFunction extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 7093626905745914312L;

    @ApiModelProperty("功能名称")
    private String functionName;
    @ApiModelProperty("别名")
    private String alias;
    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("父级ID")
    private String parentId;
    @ApiModelProperty("地址")
    private String url;
    @ApiModelProperty("是否菜单 字典YN01")
    @Dict(name = "YN01",attributes = "isMenuStr")
    private Integer isMenu;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("说明")
    private String remark;
    @ApiModelProperty("模块概述")
    private String moudeSummary;
    @ApiModelProperty("操作说明")
    private String operateInstruction;
    @ApiModelProperty("菜单级别")
    private Integer functionLevel;

    @ApiModelProperty("是否菜单")
    @TableField(exist = false)
    private String isMenuStr;
}
