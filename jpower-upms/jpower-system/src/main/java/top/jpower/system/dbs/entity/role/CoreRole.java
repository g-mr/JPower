package top.jpower.system.dbs.entity.role;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.tenant.entity.TenantEntity;

/**
 * 角色信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_role")
@EqualsAndHashCode(callSuper = true)
public class CoreRole extends TenantEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
    private Long id;
    @Schema(description = "角色别名")
    private String alias;
    @Schema(description = "角色名称")
    private String name;
    @Schema(description = "角色父级ID")
    private Long parentId;
    @Schema(description = "角色祖级ID")
    private String ancestorId;
    @Schema(description = "是否系统角色")
    private Boolean isSysRole;
    @Schema(description = "备注说明")
    private String remark;
    @Schema(description = "排序")
    private Integer sort;
}
