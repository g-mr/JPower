package com.qidiangk.smart.aster.pojo.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
@AllArgsConstructor
public class LineChatVO implements Serializable {

    @Schema(description = "X轴名称")
    private String name;

    @Schema(description = "Y轴AI数量")
    private Integer aiValue;

    @Schema(description = "Y轴坐席数量")
    private Integer attendValue;

}
