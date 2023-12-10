package top.jpower.jpower.dbs.entity.function;

import top.jpower.jpower.module.dbs.entity.base.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 顶部菜单功能关联信息
 *
 * @author mr.g
 * @date 2022/10/24 0:37
 */
@Data
public class TbCoreFunctionMenu extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6845959731076254105L;

    @ApiModelProperty("功能ID")
    private String functionId;
    @ApiModelProperty("顶部菜单ID")
    private String menuId;
}
