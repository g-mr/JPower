package com.qidiangk.smart.aster.pojo.vo.home;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * 下拉选择
 *
 * @author mr.gmac
 */
@Data
@AllArgsConstructor
public class NameSelectVO implements Serializable {

    @Schema(description = "主键")
    private String name;
    @Schema(description = "名称")
    private String showName;

}
