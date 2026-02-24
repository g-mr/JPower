package top.jpower.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 菜单返回视图
 * 
 * @author mr.g
 */
@Data
public class DataFunctionVo implements Serializable {

    private static final long serialVersionUID = 7120949826947710266L;

    private Long id;

    @ApiModelProperty("功能名称")
    private String functionName;

    @ApiModelProperty("地址")
    private String url;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("编码")
    private String code;

    @ApiModelProperty("父级ID")
    private Long parentId;

    @ApiModelProperty("别名")
    private String alias;

    @ApiModelProperty("是否有下级接口或者按钮")
    private Boolean isData;

    @ApiModelProperty("页面打开方式")
    private String targetStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
