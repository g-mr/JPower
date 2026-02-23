package top.jpower.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 选择列表
 *
 * @author mr.g
 */
@Data
public class SelectDTO implements Serializable {

	@Schema(description = "编码")
	private String code;

	@Schema(description = "名称")
	private String name;

}
