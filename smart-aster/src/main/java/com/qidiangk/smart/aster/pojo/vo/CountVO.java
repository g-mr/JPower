package com.qidiangk.smart.aster.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 汇总返回
 *
 * @author mr.g
 */
@Data
@AllArgsConstructor
public class CountVO implements Serializable {

    @Schema(description = "名称")
    private String name;
    @Schema(description = "数量")
    private Integer count;

}
