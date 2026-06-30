package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 角色权限信息
 *
 * @author mr.g
 */
@Data
public class RoleFunctionVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "角色ID")
	private Long roleId;
	@Schema(description = "功能ID")
	private Long functionId;
	@Schema(description = "功能名称")
	private String functionName;
	@Schema(description = "功能URL")
	private String url;

}
