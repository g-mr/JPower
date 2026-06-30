package com.qidiangk.smart.system.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import com.qidiangk.smart.system.dbs.entity.city.CoreCity;

/**
 * 城市视图对象
 * 
 * @author mr.g
 */
@Data
public class CityVO extends CoreCity {

    @Schema(description = "上级地区")
    private String pname;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
