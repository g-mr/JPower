package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupAttendStatusVO implements Serializable {

    @Schema(description = "坐席组名称")
    @NotBlank(message = "坐席组名称不可为空")
    String groupName;
    @Schema(description = "坐席号码")
    @NotBlank(message = "坐席号码不可为空")
    String membername;

    @Schema(description = "是否暂停（0-未暂停，1-暂停）")
    @NotNull(message = "是否暂停不可为空")
    private Boolean paused;

}
