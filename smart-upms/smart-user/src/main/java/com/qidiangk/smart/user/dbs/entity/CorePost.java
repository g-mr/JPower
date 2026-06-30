package com.qidiangk.smart.user.dbs.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import com.qidiangk.smart.common.validated.group.Validation;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

/**
 * 岗位信息
 *
 * @author mr.g
 * @date 2022-09-14 16:41
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Table("tb_core_post")
public class CorePost extends TenantEntity {

    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    @Schema(description = "主键")
    @NotNull(groups = Validation.Update.class, message = "ID不可为空")
    private Long id;
    @NotBlank(groups = {Validation.Create.class, Validation.Update.class}, message = "岗位名称不可为空")
    @Schema(description = "岗位名称")
    private String name;
    @NotBlank(groups = {Validation.Create.class, Validation.Update.class}, message = "岗位编码不可为空")
    @Schema(description = "岗位编码")
    private String code;
    @NotNull(groups = {Validation.Create.class, Validation.Update.class}, message = "岗位类型不可为空")
    @Schema(description = "岗位类型 字典：POST_TYPE")
    @Dict(name = "POST_TYPE")
    private Integer type;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "岗位描述")
    private String describe;
    @Schema(description = "上岗条件")
    private String condition;
    @Schema(description = "是否启用")
    private Boolean status;

}
