package com.qidiangk.smart.aster.pojo.vo.ivr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SelectCodeVo implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "名称")
    private String name;

    @Schema(description = "编码")
    private String code;

}
