package top.jpower.system.dbs.entity.function;

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
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;
import top.jpower.core.dbs.dictbind.annotation.Dict;

/**
 * 数据权限信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_data_scope")
@EqualsAndHashCode(callSuper = true)
public class CoreDataScope extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不能为空", groups = {Validation.Update.class})
    private Long id;
    @Schema(description = "菜单ID")
	@NotNull(message = "菜单ID不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private Long menuId;
    @Schema(description = "权限编号")
	@NotBlank(message = "权限编号不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String scopeCode;
    @Schema(description = "数据权限名称")
	@NotBlank(message = "数据权限名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String scopeName;
    @Schema(description = "数据权限类名")
	@NotBlank(message = "数据权限类名不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String scopeClass;
    @Schema(description = "数据权限字段")
    private String scopeColumn;
    @Dict(name = "DATA_SCOPE_TYPE")
    @Schema(description = "数据权限类型 字典：DATA_SCOPE_TYPE")
    private Integer scopeType;
    @Schema(description = "数据权限值域")
    private String scopeValue;
    @Schema(description = "是否所有角色都执行")
    private Boolean allRole;
    @Schema(description = "备注")
    private String note;

}
