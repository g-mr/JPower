package com.qidiangk.smart.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.jpower.core.dbs.dictbind.annotation.Dict;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能简单信息
 *
 * @author mr.g
 */
@Data
public class FunctionSimpleVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "功能ID")
	private Long id;
	@Schema(description = "父功能ID")
	private Long parentId;
	@Schema(description = "功能CODE")
	private String code;
	@Schema(description = "功能名称")
	private String functionName;
	@Schema(description = "功能别名")
	private String alias;
	@Schema(description = "功能URL")
	private String url;
	@Schema(description = "功能类型")
	@Dict(name = "FUNCTION_TYPE")
	private Integer functionType;

	@Schema(description = "其他数据")
	private Map<String, Object> params = new HashMap<>();
}
