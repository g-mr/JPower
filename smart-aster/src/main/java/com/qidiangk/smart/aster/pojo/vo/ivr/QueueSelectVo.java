package com.qidiangk.smart.aster.pojo.vo.ivr;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class QueueSelectVo implements Serializable {

    @Schema(description = "编码")
    private String name;

    @Schema(description = "名称")
    private String showName;

}
