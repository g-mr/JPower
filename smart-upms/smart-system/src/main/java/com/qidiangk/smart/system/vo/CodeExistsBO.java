package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

/**
 * code入参数
 *
 * @author mr.g
 */
@Data
public class CodeExistsBO implements Serializable {

	@Schema(description = "ID")
	private Long id;
	@Schema(description = "编码")
	@NotBlank(message = "编码不能为空")
	private String code;

}
