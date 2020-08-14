package com.wlcb.jpower.module.dbs.entity.core.city;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName tbCoreCity
 * @Description TODO 城市地区
 * @Author 郭丁志
 * @Date 2020-07-13 16:48
 * @Version 1.0
 */
@Data
public class TbCoreCity extends BaseEntity {

    private static final long serialVersionUID = 5502650422939440849L;

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("父级编码")
    private String pcode;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("全称")
    private String fullname;
    @ApiModelProperty("级别")
    private Integer rankd;
    @ApiModelProperty("经度")
    private Double lng;
    @ApiModelProperty("纬度")
    private Double lat;
    @ApiModelProperty("国家编码")
    private String countryCode;
    @ApiModelProperty("城市类型 字典值")
    @Dict(name = "CITY_TYPE",attributes = "cityTypeStr")
    private String cityType;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("排序")
    private Integer sortNum;

    @ApiModelProperty("城市类型")
    @TableField(exist = false)
    private String cityTypeStr;
}
