package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 下拉选择项
 *
 * @author mr.g
 */
@Data
public class SelectVO implements Serializable {

	@Schema(description = "CODE")
	private String code;
	@Schema(description = "名称")
	private String name;

}
