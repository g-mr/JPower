package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 菜单返回视图
 * 
 * @author mr.g
 */
@Data
public class DataFunctionVO implements Serializable {

    @Serial
	private static final long serialVersionUID = 7120949826947710266L;

    private Long id;

    @Schema(description = "功能名称")
    private String functionName;

    @Schema(description = "地址")
    private String url;

    @Schema(description = "图标")
    private String icon;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "父级ID")
    private Long parentId;

    @Schema(description = "别名")
    private String alias;

    @Schema(description = "是否有下级接口或者按钮")
    private Boolean isData;

    @Schema(description = "页面打开方式")
    private String targetStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
