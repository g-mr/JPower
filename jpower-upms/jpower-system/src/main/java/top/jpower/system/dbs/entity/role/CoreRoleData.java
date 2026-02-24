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
 * 角色数据权限信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_role_data")
@EqualsAndHashCode(callSuper = true)
public class CoreRoleData extends BaseEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	private Long id;
    @Schema(description = "角色ID")
    private Long roleId;
    @Schema(description = "数据权限ID")
    private Long dataId;

}
