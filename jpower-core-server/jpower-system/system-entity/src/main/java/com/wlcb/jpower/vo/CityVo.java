package com.wlcb.jpower.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.wlcb.jpower.dbs.entity.city.TbCoreCity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName CityVo
 * @Description TODO
 * @Author 郭丁志
 * @Date 2020/8/22 0022 0:36
 * @Version 1.0
 */
@Data
public class CityVo extends TbCoreCity{

    private static final long serialVersionUID = 3438947425188438375L;

    @ApiModelProperty("城市类型")
    private String cityTypeStr;

    @ApiModelProperty("上级地区")
    private String pname;


    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Boolean hasChildren;

}
