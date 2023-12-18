package top.jpower.jpower.dbs.entity.org;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.jpower.module.base.annotation.Dict;
import top.jpower.jpower.module.tenant.entity.TenantEntity;

/**
 * @ClassName TbCoreUser
 * @Description TODO 组织机构信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreOrg extends TenantEntity {

    private static final long serialVersionUID = 8829495593714085987L;

    @ApiModelProperty("编码")
    private String code;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("父级ID")
    private Long parentId;
    @ApiModelProperty("祖级ID")
    private String ancestorId;
    @ApiModelProperty("图标")
    private String icon;
    @ApiModelProperty("排序")
    private Integer sort;
    @ApiModelProperty("领导人名字")
    private String headName;
    @ApiModelProperty("领导人电话")
    private String headPhone;
    @ApiModelProperty("领导人邮箱")
    private String headEmail;
    @ApiModelProperty("联系人名字")
    private String contactName;
    @ApiModelProperty("联系人电话")
    private String contactPhone;
    @ApiModelProperty("联系人邮箱")
    private String contactEmail;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("机构类型 字典 ORG_TYPE")
    @Dict(name = "ORG_TYPE")
    private Integer type;
    @ApiModelProperty("备注说明")
    private String remark;

}
