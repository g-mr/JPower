package com.qidiangk.smart.system.dbs.entity.function;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

/**
 * 顶部菜单功能关联信息
 * 
 * @author mr.g
 */
@Data
@Table("tb_core_function_menu")
@EqualsAndHashCode(callSuper = true)
public class CoreFunctionMenu extends BaseEntity {

	@Schema(description = "主键")
	@Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
	private Long id;
    @Schema(description = "功能ID")
    private Long functionId;
    @Schema(description = "顶部菜单ID")
    private Long menuId;
}
