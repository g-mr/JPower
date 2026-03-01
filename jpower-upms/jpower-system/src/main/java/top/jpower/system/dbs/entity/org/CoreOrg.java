package top.jpower.system.dbs.entity.org;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.common.validated.group.Validation;
import top.jpower.core.dbs.dictbind.annotation.Dict;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

/**
 * 组织机构信息
 *
 * @author mr.g
 */
@Data
@Table("tb_core_org")
@EqualsAndHashCode(callSuper = true)
public class CoreOrg extends TenantEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "id不能为空", groups = {Validation.Update.class})
    private Long id;
    @Schema(description = "编码")
	@NotBlank(message = "编码不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String code;
    @Schema(description = "名称")
	@NotBlank(message = "名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "祖级ID")
    private String ancestorId;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "领导人名字")
    private String headName;
    @Schema(description = "领导人电话")
    private String headPhone;
    @Schema(description = "领导人邮箱")
    private String headEmail;
    @Schema(description = "联系人名字")
    private String contactName;
    @Schema(description = "联系人电话")
    private String contactPhone;
    @Schema(description = "联系人邮箱")
    private String contactEmail;
    @Schema(description = "地址")
    private String address;
    @Schema(description = "机构类型 字典 ORG_TYPE")
    @Dict(name = "ORG_TYPE")
    private Integer type;
    @Schema(description = "备注说明")
    private String remark;

}
