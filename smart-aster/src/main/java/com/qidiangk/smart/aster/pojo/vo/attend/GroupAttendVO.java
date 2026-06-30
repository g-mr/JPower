package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupAttendVO implements Serializable {

    /**
     * 成员名称（显示名称）
     */
    @Schema(description = "成员名称（显示名称）")
    private String membername;
    /**
     * 成员名称（显示名称）
     */
    @Schema(description = "成员端点名称")
    private String username;
    /**
     * 是否暂停（0-未暂停，1-暂停）
     */
    @Schema(description = "是否暂停（0-未暂停，1-暂停）")
    private Boolean paused;

}
