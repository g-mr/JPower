package com.qidiangk.smart.resource.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author mr.g
 * @date 2026-4-12 17:27
 * @description
 */
@Data
public class MoveBO implements Serializable {

	@Schema(description = "文件分组ID")
	@NotNull(message = "文件分组ID不能为空")
	private Long groupId;
	@Schema(description = "文件ID列表")
	@NotEmpty(message = "文件ID列表不能为空")
	private List<Long> ids;
}
