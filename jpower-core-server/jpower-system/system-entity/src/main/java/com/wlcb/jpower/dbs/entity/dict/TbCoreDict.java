package com.wlcb.jpower.dbs.entity.dict;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TbCoreDict
 * @Description TODO 字典
 * @Author 郭丁志
 * @Date 2020-07-13 15:47
 * @Version 1.0
 */
@Data
public class TbCoreDict extends TenantEntity {
    private static final long serialVersionUID = 2963005021265897302L;

    @ApiModelProperty("字典类型编码")
    private String dictTypeCode;
    @ApiModelProperty("字典编码")
    private String code;
    @ApiModelProperty("字典名称")
    private String name;
    @ApiModelProperty("语言类型 字典YYZL")
    @Dict(name = "YYZL",attributes = "localeStr")
    private String localeId;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("排序")
    private Integer sortNum;
    @ApiModelProperty("父级ID")
    private String parentId;
    @ApiModelProperty("级别")
    private Integer dictLevel;

}
