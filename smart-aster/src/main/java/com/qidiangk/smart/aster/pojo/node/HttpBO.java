package com.qidiangk.smart.aster.pojo.node;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author mr.g
 */
@Data
public class HttpBO implements Serializable {

    @Schema(description = "请求地址")
    @NotBlank(message = "请求地址不可为空")
    private String url;
    @Schema(description = "请求方式")
    @NotBlank(message = "请求方式不可为空")
    private String method;
    @Schema(description = "请求头")
    private Map<String, String> headers;
    @Schema(description = "请求参数")
    private String parameters;

}
