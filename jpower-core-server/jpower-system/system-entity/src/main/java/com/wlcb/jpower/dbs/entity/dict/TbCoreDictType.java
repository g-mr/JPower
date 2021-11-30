package com.wlcb.jpower.dbs.entity.dict;

import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName TbCoreDictType
 * @Description TODO 字典类型表
 * @Author 郭丁志
 * @Date 2020-07-13 15:42
 * @Version 1.0
 */
@Data
public class TbCoreDictType extends TenantEntity {

    private static final long serialVersionUID = 2104502370643051282L;

    @ApiModelProperty("字典类型编码")
    private String dictTypeCode;
    @ApiModelProperty("字典类型名称")
    private String dictTypeName;
    @ApiModelProperty("备注")
    private String note;
    @ApiModelProperty("是否允许删除 字典YN")
    @Dict(name = "YN")
    private String delEnabled;
    @ApiModelProperty("排序")
    private Integer sortNum;
    @ApiModelProperty("父级ID")
    private String parentId;
    @ApiModelProperty("是否树形结构 字典YN01")
    @Dict(name = "YN01")
    private Integer isTree;

}
