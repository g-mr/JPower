package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class AttendGroupSaveVO implements Serializable {

    /**
     * 队列名称
     */
    @Schema(description = "队列名称,不能有中文")
    @NotBlank(message = "队列名称不可为空")
    private String name;
    /**
     * 坐席成员
     */
    @Schema(description = "坐席成员ID")
    @NotNull(message = "坐席成员ID不可为空")
    private List<String> memberIds;

}
