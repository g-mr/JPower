package com.qidiangk.smart.aster.pojo.node;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.g
 */
@Data
public class LintBO implements Serializable {

    @Schema(description = "脚本类型")
    @NotBlank(message = "脚本类型不能为空")
    private String codeType;
    @Schema(description = "脚本内容")
    private String code;
    @Schema(description = "脚本入参")
    private Map<String, String> params = new HashMap<>();

}
