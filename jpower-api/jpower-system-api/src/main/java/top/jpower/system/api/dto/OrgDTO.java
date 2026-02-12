package top.jpower.system.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 组织机构信息
 *
 * @author mr.g
 */
@Data
public class OrgDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键")
    private Long id;
    @Schema(description = "编码")
    private String code;
    @Schema(description = "名称")
    private String name;
    @Schema(description = "父级ID")
    private Long parentId;
    @Schema(description = "祖级ID")
    private String ancestorId;
    @Schema(description = "图标")
    private String icon;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "领导人名字")
    private String headName;
    @Schema(description = "领导人电话")
    private String headPhone;
    @Schema(description = "领导人邮箱")
    private String headEmail;
    @Schema(description = "联系人名字")
    private String contactName;
    @Schema(description = "联系人电话")
    private String contactPhone;
    @Schema(description = "联系人邮箱")
    private String contactEmail;
    @Schema(description = "地址")
    private String address;
    @Schema(description = "机构类型 字典 ORG_TYPE")
    private Integer type;
    @Schema(description = "备注说明")
    private String remark;

}
