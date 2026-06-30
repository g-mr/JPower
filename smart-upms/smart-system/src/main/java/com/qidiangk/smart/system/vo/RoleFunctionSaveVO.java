package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * 角色权限保存VO
 *
 * @author mr.g
 */
@Data
public class RoleFunctionSaveVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "角色ID")
	@NotNull(message = "角色ID不可为空")
	private Long roleId;
	@Schema(description = "功能ID")
	private List<Long> functionIds;
	@Schema(description = "顶部菜单ID")
	private List<Long> topMenuIds;
	@Schema(description = "是否自动保存接口权限")
	private Boolean isAutoSaveInterface;
}
