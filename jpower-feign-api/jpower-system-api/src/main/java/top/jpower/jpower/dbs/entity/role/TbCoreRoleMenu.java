package top.jpower.jpower.dbs.entity.role;

import top.jpower.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 角色权限信息
 *
 * @author mr.g
 * @date 2022/10/23 23:09
 */
@Data
public class TbCoreRoleMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("角色ID")
    private String roleId;
    @ApiModelProperty("顶部菜单ID")
    private String menuId;

}
