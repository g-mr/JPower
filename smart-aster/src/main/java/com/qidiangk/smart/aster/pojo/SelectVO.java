package com.qidiangk.smart.aster.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class SelectVO implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "名称")
    private String name;

}
