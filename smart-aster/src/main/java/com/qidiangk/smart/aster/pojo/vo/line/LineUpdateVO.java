package com.qidiangk.smart.aster.pojo.vo.line;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author mr.g
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LineUpdateVO extends LineBaseVO {

    /**
     * ID
     */
    @Schema(description = "ID")
    @NotBlank(message = "ID不能为空")
    private String id;


}
