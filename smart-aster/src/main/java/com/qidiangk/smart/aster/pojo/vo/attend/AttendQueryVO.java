package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 坐席查询条件
 *
 * @author mr.g
 */
@Data
public class AttendQueryVO implements Serializable {

    @Schema(description = "号码")
    private String id;
    @Schema(description = "用户名")
    private String username;
    @Schema(description = "注册状态")
    private Boolean registerState;
}
