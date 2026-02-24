package top.jpower.system.dbs.entity.role;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 角色权限信息
 * 
 * @author mr.g
 */
@Data
@Table(value = "core_role_menu", comment = "角色顶级菜单权限信息")
@EqualsAndHashCode(callSuper = true)
public class CoreRoleMenu extends BaseEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	private Long id;
    @Schema(description = "角色ID")
    private Long roleId;
    @Schema(description = "顶部菜单ID")
    private Long menuId;

}
