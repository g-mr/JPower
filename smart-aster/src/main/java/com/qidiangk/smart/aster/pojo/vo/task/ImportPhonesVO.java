package com.qidiangk.smart.aster.pojo.vo.task;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.jpower.core.util.support.excel.Excel;

import java.io.Serializable;

/**
 * @author mr.g
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = false)
public class ImportPhonesVO implements Serializable {

    /**
     * 被叫手机号码。
     */
    @Schema(description = "被叫手机号码")
    @Excel(name = "手机号")
    private String phone;

    /**
     * 外呼时传递的初始参数，JSON 格式，用于机器人场景。
     */
    @Schema(description = "外呼时传递的初始参数，JSON 格式")
    @Excel(name = "初始参数")
    private String params;

}
