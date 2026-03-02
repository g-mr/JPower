package top.jpower.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 顶级菜单客户端ID信息
 *
 * @author mr.g
 */
@Data
public class MenuClientVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "顶级菜单ID")
	private Long id;
	@Schema(description = "顶级菜单名称")
	private String name;
	@Schema(description = "客户端ID")
	private Long clientId;
}
