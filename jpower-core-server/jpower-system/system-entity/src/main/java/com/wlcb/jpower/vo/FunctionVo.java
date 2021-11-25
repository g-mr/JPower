package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.function.TbCoreFunction;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName Function
 * @Description TODO 菜单返回试图
 * @Author 郭丁志
 * @Date 2020-07-30 10:49
 * @Version 1.0
 */
@Data
public class FunctionVo extends TbCoreFunction{

    @ApiModelProperty("是否菜单")
    private String isMenuStr;

    @ApiModelProperty("页面打开方式")
    private String targetStr;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
