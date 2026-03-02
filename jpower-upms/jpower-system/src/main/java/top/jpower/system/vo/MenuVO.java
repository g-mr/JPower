package top.jpower.system.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 顶级菜单VO
 *
 * @author mr.g
 */
@Data
public class MenuVO implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Schema(description = "id")
	private Long id;
	@Schema(description = "名称")
	private String name;
	@Schema(description = "图标")
	private String icon;
	@Schema(description = "编码")
	private String code;
	@Schema(description = "路由")
	private String router;
}
