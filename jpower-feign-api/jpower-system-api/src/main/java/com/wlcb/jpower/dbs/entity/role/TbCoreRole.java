package com.wlcb.jpower.dbs.entity.role;

import com.baomidou.mybatisplus.annotation.TableField;
import com.wlcb.jpower.module.base.annotation.Dict;
import com.wlcb.jpower.module.tenant.entity.TenantEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName TbCoreUser
 * @Description TODO 角色信息
 * @Author 郭丁志
 * @Date 2020-05-18 17:12
 * @Version 1.0
 */
@Data
public class TbCoreRole extends TenantEntity implements Serializable {

    private static final long serialVersionUID = 7093626905745914312L;

    @ApiModelProperty("角色别名")
    private String alias;
    @ApiModelProperty("角色名称")
    private String name;
    @ApiModelProperty("角色父级ID")
    private String parentId;
    @ApiModelProperty("是否系统角色 字典YN01")
    @Dict(name = "YN01",attributes = "isSysRoleStr")
    private Integer isSysRole;
    @ApiModelProperty("备注说明")
    private String remark;
    @ApiModelProperty("排序")
    private Integer sort;

    @TableField(exist = false)
    private String isSysRoleStr;
}
