package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.io.Serializable;

@Data
public class AttendVO implements Serializable {

    @Schema(description = "号码")
    @NotBlank(message = "号码不可为空")
    private String id;
    @Schema(description = "用户名")
    @NotBlank(message = "用户名不可为空")
    private String username;
    @Schema(description = "密码")
    @NotBlank(message = "密码不可为空")
    private String password;
}
