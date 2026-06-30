package com.qidiangk.smart.aster.pojo.vo.ivr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Schema(description = "呼叫路由分页")
@Data
public class RoutePageReqVO implements Serializable {

    @Schema(description = "搜索名称")
    private String name;

    @Schema(description = "搜索编码")
    private String code;

    @Schema(description = "流程类型 （1：呼入 2：呼出）")
    private Integer process;

    @Schema(description = "类型")
    private Integer type;

}
