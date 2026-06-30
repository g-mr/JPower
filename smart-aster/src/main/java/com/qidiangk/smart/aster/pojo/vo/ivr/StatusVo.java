package com.qidiangk.smart.aster.pojo.vo.ivr;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class StatusVo implements Serializable {

    @Schema(description = "ID")
    @NotNull(message = "ID 不可为空")
    private Long id;
    @Schema(description = "状态")
    @NotNull(message = "状态 不可为空")
    private Boolean status;

}
