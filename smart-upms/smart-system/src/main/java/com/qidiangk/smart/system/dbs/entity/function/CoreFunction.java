package com.qidiangk.smart.system.dbs.entity.function;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 菜单信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_function")
public class CoreFunction extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不能为空", groups = {Validation.Update.class})
    private Long id;
    @Schema(description = "客户端ID")
	@NotNull(message = "客户端ID不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private Long clientId;
    @Schema(description = "功能名称")
	@NotBlank(message = "功能名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String functionName;
    @Schema(description = "别名")
    private String alias;
    @Schema(description = "编码")
	@NotBlank(message = "编码不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String code;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "祖级ID")
    private String ancestorId;
	@Schema(description = "页面地址")
    private String route;
    @Schema(description = "页面路径")
    private String url;
    @Schema(description = "功能类型 字典：FUNCTION_TYPE")
    @Dict(name = "FUNCTION_TYPE")
	@NotNull(message = "功能类型不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private Integer functionType;
    @Schema(description = "打开方式 字典DKFS")
    @Dict(name = "DKFS")
    private String target;
    @Schema(description = "是否隐藏")
    private Boolean isHide;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "说明")
    private String remark;
    @Schema(description = "模块概述")
    private String moudeSummary;
    @Schema(description = "操作说明")
    private String operateInstruction;

}
