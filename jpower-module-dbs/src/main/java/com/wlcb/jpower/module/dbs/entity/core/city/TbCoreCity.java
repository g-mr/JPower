package com.wlcb.jpower.module.dbs.entity.core.city;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.dbs.entity.base.BaseEntity;
import lombok.Data;

/**
 * @ClassName tbCoreCity
 * @Description TODO 城市地区
 * @Author 郭丁志
 * @Date 2020-07-13 16:48
 * @Version 1.0
 */
@Data
//@Crud(path = "")
public class TbCoreCity extends BaseEntity {

    private static final long serialVersionUID = 5502650422939440849L;

    private String code;
    private String pcode;
    private String name;
    private String fullname;
    private Integer rankd;
    private Double lon;
    private Double lat;
    private String countryCode;
    @Dict(name = "CITY_TYPE",attributes = "cityTypeStr")
    private String cityType;
    private String note;
    private Integer sortNum;
    private Integer sub;

    @TableField(exist = false)
    private String cityTypeStr;
}
