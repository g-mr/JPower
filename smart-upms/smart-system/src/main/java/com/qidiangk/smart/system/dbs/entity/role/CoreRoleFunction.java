package com.qidiangk.smart.system.dbs.entity.role;

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
@Table("tb_core_role_function")
@EqualsAndHashCode(callSuper = true)
public class CoreRoleFunction extends BaseEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	private Long id;
    @Schema(description = "角色ID")
    private Long roleId;
    @Schema(description = "功能ID")
    private Long functionId;
    @Schema(description = "功能名称")
    private String functionName;
}
