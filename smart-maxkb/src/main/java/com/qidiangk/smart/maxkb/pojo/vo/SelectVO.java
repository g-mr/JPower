package com.qidiangk.smart.maxkb.pojo.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 下拉数据
 *
 * @author mr.g
 */
@Data
public class SelectVO {

    @Schema(description = "ID")
    private String id;

    @Schema(description = "名称")
    private String name;
}
