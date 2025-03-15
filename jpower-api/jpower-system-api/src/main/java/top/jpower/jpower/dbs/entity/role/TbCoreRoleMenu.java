package top.jpower.jpower.dbs.entity.role;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import top.jpower.core.dbs.dbs.entity.base.BaseEntity;

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
    private Long roleId;
    @ApiModelProperty("顶部菜单ID")
    private Long menuId;

}
