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

/**
 * 顶级菜单
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_top_menu")
@EqualsAndHashCode(callSuper = true)
public class CoreTopMenu extends BaseEntity {

    @Schema(description = "主键")
    @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	@NotNull(message = "主键不能为空", groups = {Validation.Update.class})
    private Long id;
    @Schema(description = "客户端ID")
	@NotBlank(message = "客户端ID不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private Long clientId;
    @Schema(description = "菜单编号")
	@NotBlank(message = "菜单编号不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String code;
    @Schema(description = "菜单名称")
	@NotBlank(message = "菜单名称不能为空", groups = {Validation.Create.class, Validation.Update.class})
    private String name;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "首页路由")
    private String router;
    @Schema(description = "排序")
    private Integer sortNum;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "状态")
    private Boolean status;
}
