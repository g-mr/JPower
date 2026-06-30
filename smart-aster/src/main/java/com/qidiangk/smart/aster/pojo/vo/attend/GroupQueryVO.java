package com.qidiangk.smart.aster.pojo.vo.attend;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class GroupQueryVO implements Serializable {

    /**
     * 队列名称
     */
    @Schema(description = "队列名称,不能有中文")
    private String name;
    /**
     * 显示名称
     */
    @Schema(description = "显示名称")
    private String showName;
    /**
     * 分配策略，呼叫分配给成员的策略
     */
    @Schema(description = "分配策略")
    private String strategy;

}
