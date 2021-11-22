package com.wlcb.jpower.dbs.entity.function;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TbCoreDataScope
 * @Description TODO 数据权限
 * @Author 郭丁志
 * @Date 2020-11-03 11:32
 */
@Data
public class TbCoreDataScope extends BaseEntity {

    private static final long serialVersionUID = 8094637801080068005L;

    @ApiModelProperty("菜单ID")
    private String menuId;
    @ApiModelProperty("权限编号")
    private String scopeCode;
    @ApiModelProperty("数据权限名称")
    private String scopeName;
    @ApiModelProperty("数据权限类名")
    private String scopeClass;
    @ApiModelProperty("数据权限字段")
    private String scopeColumn;
    @Dict(name = "DATA_SCOPE_TYPE")
    @ApiModelProperty("数据权限类型 字典：DATA_SCOPE_TYPE")
    private Integer scopeType;
    @ApiModelProperty("数据权限值域")
    private String scopeValue;
    @ApiModelProperty("是否所有角色都执行")
    private Integer allRole;
    @ApiModelProperty("备注")
    private String note;


}
