package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Function
 * @Description TODO 菜单返回试图
 * @Author 郭丁志
 * @Date 2020-07-30 10:49
 * @Version 1.0
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
