package com.qidiangk.smart.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 城市信息
 *
 * @author mr.g
 */
@Data
public class CityDTO implements Serializable {

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "父级编码")
    private String pcode;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "全称")
    private String fullname;
    @Schema(description = "级别")
    private Integer rankd;
    @Schema(description = "经度")
    private Double lng;
    @Schema(description = "纬度")
    private Double lat;
    @Schema(description = "国家编码")
    private String countryCode;
    @Schema(description = "城市类型 字典CITY_TYPE")
    private String cityType;
    @Schema(description = "备注")
    private String note;
    @Schema(description = "排序")
    private Integer sortNum;

}
